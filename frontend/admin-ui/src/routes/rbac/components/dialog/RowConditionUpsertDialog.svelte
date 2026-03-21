<script lang="ts">
  import { parseDate, type DateValue } from '@internationalized/date';
  import {
    Dialog,
    Select,
    PrimaryButton,
    OutlinedButton,
    IconButton,
    GhostButton,
    Switch,
    Input,
    DatePicker
  } from '@medisphere/common-ui';
  import { useRolesQuery } from '../../hooks/useRoles';
  import { useResourcesQuery } from '../../hooks/useResources';
  import type { RowCondition, ResourceField, RuleLeaf, RuleGroup, RuleNode } from '../../types';
  import { Plus, Trash2, Info, ChevronDown, ChevronUp, FolderPlus } from '@lucide/svelte';
  import { 
    parseConditionJson as jsonToGroup, 
    nodeToSentence
  } from '../../utils/condition';

  export interface Props {
    open?: boolean;
    initialData?: RowCondition;
    onClose?: () => void;
  }

  let { open = $bindable(false), initialData, onClose }: Props = $props();

  const rolesQuery = useRolesQuery();
  const resourcesQuery = useResourcesQuery();

  let showAdvanced = $state(false);
  let isSubmitting = $state(false);

  // ── Form state (plain $state, no TanStack Form) ────────────────────────────
  let formId = $state('');
  let roleId = $state('');
  let resourceId = $state('');
  let rootGroup: RuleGroup = $state(makeGroup());

  // ── Ready flag: don't render the form until queries + data are loaded ──────
  // This ensures Select components always mount with options already available
  let isReady = $state(false);

  const roleOptions = $derived(
    (rolesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );
  const resourceOptions = $derived(
    (resourcesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );
  const fields = $derived(
    (resourcesQuery.data ?? []).find((r) => r.id === resourceId)?.fields ?? []
  );
  const fieldOptions = $derived(fields.map((f) => ({ value: f.fieldName, label: f.fieldName })));
  const isFormComplete = $derived(!!(roleId && resourceId && rootGroup.children.length > 0));

  const OPERATORS_BY_TYPE: Record<string, { label: string; value: string }[]> = {
    text: [
      { label: 'Equals', value: 'eq' },
      { label: 'Contains', value: 'like' },
      { label: 'Not Equals', value: 'ne' },
      { label: 'Starts With', value: 'sw' }
    ],
    number: [
      { label: '=', value: 'eq' },
      { label: '>', value: 'gt' },
      { label: '<', value: 'lt' },
      { label: '>=', value: 'gte' },
      { label: '<=', value: 'lte' },
      { label: '!=', value: 'ne' }
    ],
    date: [
      { label: 'On', value: 'eq' },
      { label: 'Before', value: 'lt' },
      { label: 'After', value: 'gt' },
      { label: 'Between', value: 'between' }
    ],
    enum: [
      { label: 'Is', value: 'eq' },
      { label: 'Is Not', value: 'ne' },
      { label: 'In', value: 'in' }
    ]
  };


  // ── Factories ──────────────────────────────────────────────────────────────

  function makeLeaf(overrides?: Partial<RuleLeaf>): RuleLeaf {
    return { kind: 'rule', id: crypto.randomUUID(), field: '', op: 'eq', value: '', ...overrides };
  }

  function makeGroup(overrides?: { logicalOp?: 'AND' | 'OR'; children?: RuleNode[] }): RuleGroup {
    return {
      kind: 'group',
      id: crypto.randomUUID(),
      logicalOp: overrides?.logicalOp ?? 'AND',
      children: overrides?.children ?? [makeLeaf()]
    };
  }

  // ── JSON ↔ RuleGroup ───────────────────────────────────────────────────────
  // (handled by shared utility)

  function groupToJson(group: RuleGroup, pretty = false, hideId = false): string {
    function clean(node: RuleNode): Record<string, unknown> | null {
      if (node.kind === 'rule') {
        if (!(node.field && node.value !== '' && node.value !== undefined)) return null;
        if (hideId) {
          return {
            kind: node.kind,
            field: node.field,
            op: node.op,
            value: node.value
          };
        }
        return node as unknown as Record<string, unknown>;
      }
      const kids = node.children.map(clean).filter((c): c is Record<string, unknown> => c !== null);
      if (!kids.length) return null;
      if (hideId) {
        return {
          kind: node.kind,
          logicalOp: node.logicalOp,
          children: kids
        };
      }
      return { ...node, children: kids };
    }

    const cleaned = clean(group) ?? {
      kind: 'group' as const,
      ...(hideId ? {} : { id: group.id }),
      logicalOp: group.logicalOp,
      children: []
    };
    return JSON.stringify(cleaned, null, pretty ? 2 : 0);
  }

  // ── Sentence preview ───────────────────────────────────────────────────────
  // (handled by shared utility)

  function getPreviewSentence(): string {
    const roleName = roleOptions.find((r) => r.value === roleId)?.label ?? 'Role';
    const resourceName = resourceOptions.find((r) => r.value === resourceId)?.label ?? 'Resource';
    const sentence = nodeToSentence(rootGroup);
    return sentence === '…'
      ? `Define rules for ${roleName} accessing ${resourceName}`
      : `${roleName} can access ${resourceName} where ${sentence}`;
  }

  // ── Lifecycle ──────────────────────────────────────────────────────────────
  // Wait until both queries are done AND the dialog is open before showing the
  // form. This guarantees Select components mount with options already in place.

  let prevOpen = false;

  $effect(() => {
    const queriesReady = !rolesQuery.isPending && !resourcesQuery.isPending;

    if (open && !prevOpen) {
      // Dialog just opened — wait for queries then initialise
      isReady = false;

      if (queriesReady) {
        initForm();
      }
      // If queries aren't ready yet, the effect below will fire when they settle
    }

    if (!open && prevOpen) {
      isReady = false;
    }

    prevOpen = open;
  });

  // Separate effect: fires whenever query loading state changes while dialog is open
  $effect(() => {
    const queriesReady = !rolesQuery.isPending && !resourcesQuery.isPending;
    if (open && queriesReady && !isReady) {
      initForm();
    }
  });

  function initForm() {
    if (initialData) {
      formId = initialData.id;
      roleId = initialData.roleId;
      resourceId = initialData.resourceId;
      rootGroup = jsonToGroup(initialData.conditionJson);
    } else {
      formId = '';
      roleId = '';
      resourceId = '';
      rootGroup = makeGroup();
    }
    isReady = true;
  }

  // ── Tree mutators ──────────────────────────────────────────────────────────

  function updateNode(root: RuleGroup, id: string, updater: (n: RuleNode) => RuleNode): RuleGroup {
    function walk(node: RuleNode): RuleNode {
      if (node.id === id) return updater(node);
      if (node.kind === 'group') return { ...node, children: node.children.map(walk) };
      return node;
    }
    return walk(root) as RuleGroup;
  }

  function deleteNode(root: RuleGroup, id: string): RuleGroup {
    function walk(node: RuleNode): RuleNode | null {
      if (node.id === id) return null;
      if (node.kind === 'group') {
        const children = node.children.map(walk).filter((c): c is RuleNode => c !== null);
        return { ...node, children };
      }
      return node;
    }
    return walk(root) as RuleGroup;
  }

  function mutate(updater: (g: RuleGroup) => RuleGroup) {
    rootGroup = updater(rootGroup);
  }

  function addLeafToGroup(groupId: string) {
    mutate((root) =>
      updateNode(root, groupId, (n) =>
        n.kind === 'group' ? { ...n, children: [...n.children, makeLeaf()] } : n
      )
    );
  }

  function addGroupToGroup(groupId: string) {
    mutate((root) =>
      updateNode(root, groupId, (n) =>
        n.kind === 'group' ? { ...n, children: [...n.children, makeGroup()] } : n
      )
    );
  }

  function handleLeafFieldChange(leafId: string, fieldName: string) {
    const f = fields.find((f) => f.fieldName === fieldName);
    const defaultOp = OPERATORS_BY_TYPE[f?.fieldType ?? 'text'][0].value;
    mutate((root) =>
      updateNode(root, leafId, (n) =>
        n.kind === 'rule' ? { ...n, field: fieldName, op: defaultOp, value: '' } : n
      )
    );
  }

  function handleLeafOpChange(leafId: string, op: string) {
    mutate((root) => updateNode(root, leafId, (n) => (n.kind === 'rule' ? { ...n, op } : n)));
  }

  function handleLeafValueChange(leafId: string, value: unknown) {
    mutate((root) => updateNode(root, leafId, (n) => (n.kind === 'rule' ? { ...n, value } : n)));
  }

  function handleGroupLogicalOpChange(groupId: string, logicalOp: 'AND' | 'OR') {
    mutate((root) =>
      updateNode(root, groupId, (n) => (n.kind === 'group' ? { ...n, logicalOp } : n))
    );
  }

  function handleResourceChange(val: string) {
    if (val !== resourceId) {
      resourceId = val;
      rootGroup = makeGroup();
    }
  }

  // ── Submit ─────────────────────────────────────────────────────────────────

  async function handleSubmit() {
    if (!roleId || !resourceId) return;
    isSubmitting = true;
    try {
      const finalJson = groupToJson(rootGroup);
      console.log('Submit', { id: formId, roleId, resourceId, conditionJson: finalJson });
      open = false;
      onClose?.();
    } finally {
      isSubmitting = false;
    }
  }
</script>

<!-- ── Recursive group snippet ─────────────────────────────────────────────── -->

{#snippet renderGroup(group: RuleGroup, allFields: ResourceField[], depth: number)}
  <div
    class="space-y-3 rounded-xl border p-4 shadow-sm {depth === 0
      ? 'bg-card'
      : 'border-primary/25 bg-primary/5'}"
  >
    <!-- Group header -->
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2 rounded-full border bg-muted px-3 py-1.5 shadow-sm">
        <span
          class="text-[10px] font-bold transition-colors {group.logicalOp === 'AND'
            ? 'text-primary'
            : 'text-muted-foreground'}">AND</span
        >
        <Switch
          checked={group.logicalOp === 'OR'}
          onCheckedChange={(v: boolean) => handleGroupLogicalOpChange(group.id, v ? 'OR' : 'AND')}
        />
        <span
          class="text-[10px] font-bold transition-colors {group.logicalOp === 'OR'
            ? 'text-primary'
            : 'text-muted-foreground'}">OR</span
        >
      </div>

      <div class="flex items-center gap-1">
        <GhostButton
          onclick={() => addLeafToGroup(group.id)}
          disabled={!resourceId}
          class="flex h-7 items-center gap-1 px-2 text-xs text-muted-foreground hover:text-foreground"
        >
          <Plus class="h-3 w-3" /> Rule
        </GhostButton>

        {#if depth < 2}
          <GhostButton
            onclick={() => addGroupToGroup(group.id)}
            disabled={!resourceId}
            class="flex h-7 items-center gap-1 px-2 text-xs text-muted-foreground hover:text-foreground"
          >
            <FolderPlus class="h-3 w-3" /> Group
          </GhostButton>
        {/if}

        {#if depth > 0}
          <IconButton
            variant="ghost"
            size="icon-sm"
            onclick={() => mutate((root) => deleteNode(root, group.id))}
            ariaLabel="Remove group"
            class="text-muted-foreground hover:text-destructive"
          >
            <Trash2 class="h-3 w-3" />
          </IconButton>
        {/if}
      </div>
    </div>

    <!-- Children -->
    <div class="space-y-3 {depth > 0 ? 'pl-3' : ''}">
      {#each group.children as child (child.id)}
        {#if child.kind === 'group'}
          {@render renderGroup(child, allFields, depth + 1)}
        {:else}
          {@const leaf = child as RuleLeaf}
          {@const fieldType =
            allFields.find((f) => f.fieldName === leaf.field)?.fieldType ?? 'text'}
          {@const fieldDef = allFields.find((f) => f.fieldName === leaf.field)}

          <div class="group flex items-start gap-3">
            <div class="grid flex-1 grid-cols-3 gap-3">
              <!-- Field -->
              <div class="space-y-1">
                {#if depth === 0}
                  <label
                    class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                    for={`leaf_field_${leaf.id}`}>Field</label
                  >
                {/if}
                <Select
                  name={`leaf_field_${leaf.id}`}
                  placeholder="Select field"
                  options={fieldOptions}
                  disabled={!resourceId}
                  value={leaf.field}
                  onValueChange={(val: string) => handleLeafFieldChange(leaf.id, val)}
                />
              </div>

              <!-- Operator -->
              <div class="space-y-1">
                {#if depth === 0}
                  <label
                    class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                    for={`leaf_op_${leaf.id}`}>Operator</label
                  >
                {/if}
                <Select
                  name={`leaf_op_${leaf.id}`}
                  placeholder="Op"
                  options={OPERATORS_BY_TYPE[fieldType]}
                  disabled={!leaf.field}
                  value={leaf.op}
                  onValueChange={(val: string) => handleLeafOpChange(leaf.id, val)}
                />
              </div>

              <!-- Value -->
              <div class="space-y-1">
                {#if depth === 0}
                  <label
                    class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                    for={`leaf_value_${leaf.id}`}>Value</label
                  >
                {/if}
                {#if fieldDef?.fieldType === 'enum'}
                  <Select
                    name={`leaf_value_${leaf.id}`}
                    placeholder="Value"
                    options={fieldDef.options?.map((o) => ({ value: o, label: o })) ?? []}
                    disabled={!leaf.field}
                    value={leaf.value as string | undefined}
                    onValueChange={(val: string) => handleLeafValueChange(leaf.id, val)}
                  />
                {:else if fieldDef?.fieldType === 'date'}
                  <DatePicker
                    disabled={!leaf.field}
                    value={leaf.value ? parseDate(leaf.value as string) : undefined}
                    onValueChange={(val: DateValue | undefined) => handleLeafValueChange(leaf.id, val?.toString())}
                  />
                {:else if fieldDef?.fieldType === 'number'}
                  <Input
                    type="number"
                    placeholder="0"
                    disabled={!leaf.field}
                    value={leaf.value as number}
                    oninput={(e: Event) => handleLeafValueChange(leaf.id, (e.target as HTMLInputElement).valueAsNumber)}
                  />
                {:else}
                  <Input
                    type="text"
                    placeholder="Value"
                    disabled={!leaf.field}
                    value={leaf.value as string}
                    oninput={(e: Event) => handleLeafValueChange(leaf.id, (e.target as HTMLInputElement).value)}
                  />
                {/if}
              </div>
            </div>

            {#if group.children.length > 1}
              <IconButton
                variant="ghost"
                size="icon-sm"
                onclick={() => mutate((root) => deleteNode(root, leaf.id))}
                ariaLabel="Remove rule"
                class="mt-6 text-muted-foreground opacity-0 transition-all group-hover:opacity-100 hover:text-destructive"
              >
                <Trash2 class="h-4 w-4" />
              </IconButton>
            {/if}
          </div>
        {/if}
      {/each}
    </div>
  </div>
{/snippet}

<!-- ── Dialog ──────────────────────────────────────────────────────────────── -->

<Dialog
  bind:open
  title={initialData ? 'Edit Row Condition' : 'Create Row Condition'}
  description="Define rules to control which rows this role can access."
  class="sm:max-w-[750px]"
>
  {#if !isReady}
    <!-- Loading state while queries settle -->
    <div class="flex h-48 items-center justify-center">
      <div class="flex flex-col items-center gap-2 text-muted-foreground">
        <div
          class="h-6 w-6 animate-spin rounded-full border-2 border-primary border-t-transparent"
        ></div>
        <span class="text-xs">Loading...</span>
      </div>
    </div>
  {:else}
    <!-- Full form — only mounts once both queries are resolved and state is initialised.
         No {#key} needed here; isReady going false→true already unmounts/remounts. -->
    <form
      onsubmit={(e) => {
        e.preventDefault();
        e.stopPropagation();
        handleSubmit();
      }}
      class="space-y-6 pt-2"
    >
      <div class="max-h-[65vh] space-y-6 overflow-y-auto px-1">
        <!-- Role + Resource -->
        <div class="mt-2 grid grid-cols-2 gap-6 rounded-xl border bg-muted/30 p-4">
          <Select
            name="roleId"
            label="Role"
            placeholder="Select a role..."
            options={roleOptions}
            value={roleId}
            required
            onValueChange={(val: string) => {
              roleId = val;
            }}
          />
          <Select
            name="resourceId"
            label="Resource"
            placeholder="Select a resource..."
            options={resourceOptions}
            value={resourceId}
            required
            onValueChange={handleResourceChange}
          />
        </div>

        <!-- Rule Builder -->
        <div class="space-y-2">
          <h3 class="text-sm font-semibold text-foreground/80">Rules</h3>
          {@render renderGroup(rootGroup, fields, 0)}
        </div>

        <!-- Preview -->
        <div
          class="relative overflow-hidden rounded-2xl border bg-primary/5 p-6 shadow-inner ring-1 ring-primary/10"
        >
          <div class="absolute -top-4 -right-4 text-primary/5">
            <Info class="h-24 w-24" />
          </div>
          <div class="relative flex flex-col gap-2">
            <span
              class="flex items-center gap-2 text-[10px] font-bold tracking-[0.2em] text-primary/60 uppercase"
            >
              <Info class="h-3 w-3" />
              Rule Preview
            </span>
            <p class="text-lg font-medium tracking-tight text-foreground/90 italic">
              "{getPreviewSentence()}"
            </p>
          </div>
        </div>

        <!-- Developer JSON -->
        <div class="space-y-2 pt-2">
          <GhostButton
            onclick={() => (showAdvanced = !showAdvanced)}
            class="flex items-center gap-2 px-1 text-[10px] font-bold tracking-widest text-muted-foreground/60 uppercase transition-colors hover:text-primary"
          >
            {#if showAdvanced}
              <ChevronUp class="h-3 w-3" />
            {:else}
              <ChevronDown class="h-3 w-3" />
            {/if}
            Developer View (JSON)
          </GhostButton>

          {#if showAdvanced}
            <div class="rounded-xl border bg-muted/40 p-4 font-mono shadow-inner">
              <pre
                class="max-h-48 overflow-auto text-[10px] leading-relaxed text-muted-foreground">{groupToJson(
                  rootGroup,
                  true,
                  true
                )}</pre>
            </div>
          {/if}
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end gap-3 border-t pt-6">
        <OutlinedButton
          type="button"
          onclick={() => {
            open = false;
            onClose?.();
          }}
        >
          Cancel
        </OutlinedButton>
        <PrimaryButton type="submit" disabled={isSubmitting || !isFormComplete} class="px-8">
          {#if isSubmitting}
            Saving...
          {:else}
            {initialData ? 'Update Condition' : 'Create Condition'}
          {/if}
        </PrimaryButton>
      </div>
    </form>
  {/if}
</Dialog>
