<script lang="ts">
  import {
    Dialog,
    Select,
    PrimaryButton,
    OutlinedButton,
    Input,
    Switch
  } from '@medisphere/common-ui';
  import { createForm } from '@tanstack/svelte-form';
  import { z } from 'zod';
  import { useRolesQuery } from '../../hooks/useRoles';
  import { useResourcesQuery } from '../../hooks/useResources';
  import type { RowCondition, ResourceField } from '../../types';
  import { Plus, Trash2, Info, ChevronDown, ChevronUp } from '@lucide/svelte';

  export interface Props {
    open?: boolean;
    initialData?: RowCondition;
    onClose?: () => void;
  }

  let { open = $bindable(false), initialData, onClose }: Props = $props();

  const rolesQuery = useRolesQuery();
  const resourcesQuery = useResourcesQuery();

  let showAdvanced = $state(false);
  let loadedResourceId = $state('');
  let isInitializing = $state(false);
  let isInitialized = $state(false);
  let renderKey = $state(0); // incrementing this forces the form UI to remount

  const roleOptions = $derived(
    (rolesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );
  const resourceOptions = $derived(
    (resourcesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );

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

  const ALL_OPERATORS = Object.values(OPERATORS_BY_TYPE).flat();

  type Rule = {
    id: string;
    field: string;
    op: string;
    value: unknown;
  };

  function jsonToRules(json: unknown): { rules: Rule[]; logicalOp: 'AND' | 'OR' } {
    try {
      if (!json || json === '{}') {
        return {
          rules: [{ id: crypto.randomUUID(), field: '', op: 'eq', value: '' }],
          logicalOp: 'AND'
        };
      }

      const parsed = typeof json === 'string' ? JSON.parse(json) : json;

      if (parsed.rules && Array.isArray(parsed.rules)) {
        return {
          rules: parsed.rules.map(
            (r: { id: unknown; field: unknown; op: unknown; value: unknown }) => ({
              id: r.id || crypto.randomUUID(),
              field: r.field || '',
              op: r.op || 'eq',
              value: r.value ?? ''
            })
          ),
          logicalOp: parsed.logicalOp || 'AND'
        };
      }

      const rules: Rule[] = [];
      Object.entries(parsed).forEach(([field, data]) => {
        if (field === 'logicalOp') return;
        const ruleData = data as { op?: string; value?: unknown };
        if (ruleData && typeof ruleData === 'object' && 'op' in ruleData) {
          rules.push({
            id: crypto.randomUUID(),
            field,
            op: ruleData.op || 'eq',
            value: ruleData.value ?? ''
          });
        }
      });

      return {
        rules: rules.length ? rules : [{ id: crypto.randomUUID(), field: '', op: 'eq', value: '' }],
        logicalOp: parsed.logicalOp || 'AND'
      };
    } catch {
      return {
        rules: [{ id: crypto.randomUUID(), field: '', op: 'eq', value: '' }],
        logicalOp: 'AND'
      };
    }
  }

  function rulesToJson(rules: Rule[], logicalOp: 'AND' | 'OR', isPreview: boolean = false): string {
    const validRules = rules.filter((r) => r.field && r.value !== undefined && r.value !== '');

    const normalizedRules = isPreview
      ? validRules.map((v) => ({
          field: v.field,
          op: v.op,
          value: v.value
        }))
      : validRules;

    return JSON.stringify(
      {
        logicalOp,
        rules: normalizedRules
      },
      null,
      2
    );
  }

  const rowConditionSchema = z.object({
    id: z.string(),
    roleId: z.string().min(1),
    resourceId: z.string().min(1),
    logicalOp: z.enum(['AND', 'OR']),
    rules: z.array(
      z.object({
        id: z.string(),
        field: z.string().min(1),
        op: z.string().min(1),
        value: z.unknown()
      })
    ),
    conditionJson: z.string()
  });

  type RowConditionFormValues = z.infer<typeof rowConditionSchema>;

  const form = createForm(() => ({
    defaultValues: {
      id: initialData?.id ?? '',
      roleId: initialData?.roleId ?? '',
      resourceId: initialData?.resourceId ?? '',
      logicalOp: 'AND',
      rules: [{ id: crypto.randomUUID(), field: '', op: 'eq', value: '' }],
      conditionJson: initialData?.conditionJson ?? '{}'
    } as RowConditionFormValues,
    onSubmit: async ({ value }) => {
      const finalJson = rulesToJson(value.rules, value.logicalOp);
      console.log('Submit', finalJson);
      open = false;
      onClose?.();
    },
    validators: {
      onChange: ({ value }) => rowConditionSchema.safeParse(value).error?.issues[0]?.message
    }
  }));

  const getFields = (resourceId: string): ResourceField[] => {
    return (resourcesQuery.data ?? []).find((r) => r.id === resourceId)?.fields ?? [];
  };

  $effect(() => {
    if (open) {
      if (!isInitialized) {
        isInitialized = true;
        isInitializing = true;

        if (initialData) {
          const parsed = jsonToRules(initialData.conditionJson);
          loadedResourceId = initialData.resourceId ?? '';

          form.reset({
            id: initialData.id,
            roleId: initialData.roleId ?? '',
            resourceId: initialData.resourceId ?? '',
            logicalOp: parsed.logicalOp,
            rules: parsed.rules,
            conditionJson: initialData.conditionJson
          });
        } else {
          loadedResourceId = '';
          form.reset();
        }

        // Increment renderKey to force the entire form UI to remount,
        // so all Select components pick up the new values from scratch.
        // Then clear isInitializing after a tick.
        renderKey += 1;
        setTimeout(() => {
          isInitializing = false;
        }, 50);
      }
    } else {
      if (isInitialized) {
        isInitialized = false;
        form.reset();
        loadedResourceId = '';
      }
    }
  });

  function addRule() {
    form.setFieldValue('rules', (old) => [
      ...old,
      { id: crypto.randomUUID(), field: '', op: 'eq', value: '' }
    ]);
  }

  function removeRule(index: number) {
    form.setFieldValue('rules', (old) => old.filter((_, i) => i !== index));
  }

  function handleFieldChange(index: number, fieldName: string, fields: ResourceField[]) {
    // Guard against firing during form initialization / reset
    if (isInitializing) return;

    const field = fields.find((f) => f.fieldName === fieldName);
    const defaultOp = OPERATORS_BY_TYPE[field?.fieldType ?? 'text'][0].value;

    form.setFieldValue(`rules[${index}].field`, fieldName);
    form.setFieldValue(`rules[${index}].op`, defaultOp);
    form.setFieldValue(`rules[${index}].value`, '');
  }

  function getSentence(values: RowConditionFormValues) {
    const roleName = roleOptions.find((r) => r.value === values.roleId)?.label ?? 'Role';
    const resourceName =
      resourceOptions.find((r) => r.value === values.resourceId)?.label ?? 'Resource';

    const ruleTexts = values.rules
      .filter((r) => r.field && r.value)
      .map((r) => {
        const opLabel = ALL_OPERATORS.find((o) => o.value === r.op)?.label ?? r.op;
        return `${r.field} ${opLabel.toLowerCase()} "${r.value}"`;
      });

    if (ruleTexts.length === 0) {
      return `Define rules for ${roleName} accessing ${resourceName}`;
    }

    const rulesStr =
      ruleTexts.length > 1 ? ruleTexts.join(` ${values.logicalOp.toLowerCase()} `) : ruleTexts[0];

    return `${roleName} can access ${resourceName} where ${rulesStr}`;
  }
</script>

<Dialog
  bind:open
  title={initialData ? 'Edit Row Condition' : 'Create Row Condition'}
  description="Define rules to control which rows this role can access."
  class="sm:max-w-[700px]"
>
  {#key renderKey}
    <form
      onsubmit={(e) => {
        e.preventDefault();
        e.stopPropagation();
        form.handleSubmit();
      }}
      class="space-y-6 pt-2"
    >
      <form.Subscribe selector={(s) => s.values}>
        {#snippet children(values)}
          {@const fields = getFields(values.resourceId)}
          {@const fieldOptions = fields.map((f) => ({ value: f.fieldName, label: f.fieldName }))}
          {@const isFormComplete = !!(
            values.roleId &&
            values.resourceId &&
            values.rules.every((r) => r.field && r.op && r.value)
          )}

          <div class="max-h-[60vh] space-y-6 overflow-y-auto px-1">
            <!-- Context Selection -->
            <div class="mt-2 grid grid-cols-2 gap-6 rounded-xl border bg-muted/30 p-4">
              <Select
                formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                name="roleId"
                label="Role"
                placeholder="Select a role..."
                options={roleOptions}
                required
              />
              <Select
                formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                name="resourceId"
                label="Resource"
                placeholder="Select a resource..."
                options={resourceOptions}
                required
                onValueChange={(val: string) => {
                  // Guard against firing during initialization
                  if (isInitializing) return;
                  if (val && val !== loadedResourceId) {
                    loadedResourceId = val;
                    form.setFieldValue('rules', [
                      { id: crypto.randomUUID(), field: '', op: 'eq', value: '' }
                    ]);
                  }
                }}
              />
            </div>

            <!-- Rule Builder -->
            <div class="space-y-4">
              <div class="flex items-center justify-between">
                <h3 class="text-sm font-semibold text-foreground/80">Rules</h3>
                {#if values.rules.length > 1}
                  <div
                    class="flex items-center gap-2 rounded-full border bg-muted px-3 py-1.5 shadow-sm"
                  >
                    <span
                      class="text-[10px] font-bold {values.logicalOp === 'AND'
                        ? 'text-primary'
                        : 'text-muted-foreground'} transition-colors">AND</span
                    >
                    <Switch
                      checked={values.logicalOp === 'OR'}
                      onCheckedChange={(checked: boolean) =>
                        form.setFieldValue('logicalOp', checked ? 'OR' : 'AND')}
                    />
                    <span
                      class="text-[10px] font-bold {values.logicalOp === 'OR'
                        ? 'text-primary'
                        : 'text-muted-foreground'} transition-colors">OR</span
                    >
                  </div>
                {/if}
              </div>

              <div class="space-y-3">
                {#each values.rules as rule, i (rule.id)}
                  {@const fieldType =
                    fields.find((f) => f.fieldName === rule.field)?.fieldType ?? 'text'}
                  {@const field = fields.find((f) => f.fieldName === rule.field)}
                  <div
                    class="group relative flex items-start gap-3 rounded-xl border bg-card p-4 shadow-sm transition-all hover:shadow-md"
                  >
                    <div class="grid flex-1 grid-cols-3 gap-4">
                      <!-- Field -->
                      <div class="space-y-1.5">
                        <label
                          for={`field-${i}`}
                          class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                          >Field</label
                        >
                        <Select
                          formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                          name={`rules[${i}].field`}
                          placeholder="Select field"
                          options={fieldOptions}
                          disabled={!values.resourceId}
                          value={rule.field}
                          onValueChange={(val: string) => {
                            // handleFieldChange already guards with isInitializing
                            handleFieldChange(i, val, fields);
                          }}
                        />
                      </div>

                      <!-- Operator -->
                      <div class="space-y-1.5">
                        <label
                          for={`op-${i}`}
                          class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                          >Operator</label
                        >
                        <Select
                          formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                          name={`rules[${i}].op`}
                          placeholder="Op"
                          options={OPERATORS_BY_TYPE[fieldType]}
                          disabled={!rule.field}
                          value={rule.op}
                          onValueChange={(val: string) => {
                            if (isInitializing) return;
                            form.setFieldValue(`rules[${i}].op`, val);
                          }}
                        />
                      </div>

                      <!-- Value -->
                      <div class="space-y-1.5">
                        <label
                          for={`value-${i}`}
                          class="text-[10px] font-bold tracking-wider text-muted-foreground uppercase"
                          >Value</label
                        >
                        {#if field?.fieldType === 'enum'}
                          <Select
                            formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                            name={`rules[${i}].value`}
                            placeholder="Value"
                            options={field.options?.map((o) => ({ value: o, label: o })) ?? []}
                            disabled={!rule.field}
                            value={rule.value as string | undefined}
                            onValueChange={(val: string) => {
                              if (isInitializing) return;
                              form.setFieldValue(`rules[${i}].value`, val);
                            }}
                          />
                        {:else if field?.fieldType === 'date'}
                          <Input
                            formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                            name={`rules[${i}].value`}
                            type="date"
                            disabled={!rule.field}
                          />
                        {:else if field?.fieldType === 'number'}
                          <Input
                            formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                            name={`rules[${i}].value`}
                            type="number"
                            placeholder="0"
                            disabled={!rule.field}
                          />
                        {:else}
                          <Input
                            formApi={form as unknown as import('@medisphere/common-ui').AnyTanStackFormApi}
                            name={`rules[${i}].value`}
                            placeholder="Value"
                            disabled={!rule.field}
                          />
                        {/if}
                      </div>
                    </div>

                    {#if values.rules.length > 1}
                      <button
                        type="button"
                        onclick={() => removeRule(i)}
                        class="mt-6 text-muted-foreground transition-all group-hover:opacity-100 hover:text-destructive sm:opacity-0"
                        title="Remove rule"
                      >
                        <Trash2 class="h-4 w-4" />
                      </button>
                    {/if}
                  </div>
                {/each}

                <button
                  type="button"
                  onclick={addRule}
                  disabled={!values.resourceId}
                  class="flex w-full items-center justify-center gap-2 rounded-xl border border-dashed py-4 text-sm font-medium text-muted-foreground transition-all hover:border-solid hover:bg-muted/40 hover:text-foreground disabled:opacity-40"
                >
                  <Plus class="h-4 w-4" />
                  Add rule
                </button>
              </div>
            </div>

            <!-- Preview Section -->
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
                  "{getSentence(values)}"
                </p>
              </div>
            </div>

            <!-- Advanced JSON Tool -->
            <div class="space-y-2 pt-2">
              <button
                type="button"
                onclick={() => (showAdvanced = !showAdvanced)}
                class="flex items-center gap-2 px-1 text-[10px] font-bold tracking-widest text-muted-foreground/60 uppercase transition-colors hover:text-primary"
              >
                {#if showAdvanced}<ChevronUp class="h-3 w-3" />{:else}<ChevronDown
                    class="h-3 w-3"
                  />{/if}
                Developer View (JSON)
              </button>

              {#if showAdvanced}
                <div class="rounded-xl border bg-muted/40 p-4 font-mono shadow-inner">
                  <pre
                    class="max-h-32 overflow-auto text-[10px] leading-relaxed text-muted-foreground">{rulesToJson(
                      values.rules,
                      values.logicalOp,
                      true
                    )}</pre>
                </div>
              {/if}
            </div>
          </div>

          <div class="flex justify-end gap-3 border-t pt-6">
            <OutlinedButton
              type="button"
              onclick={() => {
                open = false;
                if (onClose) onClose();
              }}
            >
              Cancel
            </OutlinedButton>
            <PrimaryButton
              type="submit"
              disabled={form.state.isSubmitting || !isFormComplete}
              class="px-8"
            >
              {#if form.state.isSubmitting}
                Saving...
              {:else}
                {initialData ? 'Update Condition' : 'Create Condition'}
              {/if}
            </PrimaryButton>
          </div>
        {/snippet}
      </form.Subscribe>
    </form>
  {/key}
</Dialog>
