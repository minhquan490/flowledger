<script lang="ts">
  import { IconButton, PrimaryButton, Chip, Badge, type DataTableColumn } from '@medisphere/common-ui';
  import { SmartTable } from '$lib/components';
  import { Pencil, Trash2, Plus, Info, LayoutList, ShieldAlert } from '@lucide/svelte';
  import { useRowConditionsQuery } from '../../hooks/useRowConditions';
  import { useRolesQuery } from '../../hooks/useRoles';
  import { useResourcesQuery } from '../../hooks/useResources';
  import RowConditionUpsertDialog from '../dialog/RowConditionUpsertDialog.svelte';
  import type { RowCondition } from '../../types';
  import { 
    parseConditionJson, 
    getAccessLogicPreview, 
    OPERATOR_LABELS, 
    flattenRules 
  } from '../../utils/condition';

  const rowConditionsQuery = useRowConditionsQuery();
  const rolesQuery = useRolesQuery();
  const resourcesQuery = useResourcesQuery();

  const columns: DataTableColumn[] = [
    { key: 'roleName', label: 'Role', class: 'font-medium' },
    { key: 'resourceName', label: 'Resource' },
    { key: 'conditions', label: 'Access Rules', class: 'hidden md:table-cell' },
    { key: 'updatedAt', label: 'Last Modified', class: 'text-muted-foreground w-[120px]' },
    { key: 'actions', label: '', class: 'w-[80px] text-right' }
  ];

  let dialogOpen = $state(false);
  let editingCondition: RowCondition | undefined = $state(undefined);

  function openCreateDialog() {
    editingCondition = undefined;
    dialogOpen = true;
  }

  function openEditDialog(item: RowCondition) {
    editingCondition = item;
    dialogOpen = true;
  }

  function getSentence(item: RowCondition) {
    return getAccessLogicPreview(item.roleName, item.resourceName, item.conditionJson);
  }
</script>

<div class="space-y-4">
  {#if rowConditionsQuery.isPending || rolesQuery.isPending || resourcesQuery.isPending}
    <div class="flex h-[400px] items-center justify-center rounded-xl border border-dashed bg-muted/20">
      <div class="flex flex-col items-center gap-2 text-muted-foreground">
        <div class="h-8 w-8 animate-spin rounded-full border-2 border-primary border-t-transparent"></div>
        <span class="text-xs font-medium">Loading ledger conditions...</span>
      </div>
    </div>
  {:else if rowConditionsQuery.isError}
    <div class="flex h-[400px] flex-col items-center justify-center gap-4 rounded-xl border border-destructive/20 bg-destructive/5 text-destructive">
      <ShieldAlert class="h-10 w-10" />
      <div class="text-center">
        <p class="font-semibold">Failed to load conditions</p>
        <p class="text-sm opacity-80">{rowConditionsQuery.error.message}</p>
      </div>
    </div>
  {:else if rowConditionsQuery.data}
    <SmartTable
      {columns}
      data={rowConditionsQuery.data}
      rowKey="id"
      searchKeys={['roleName', 'resourceName']}
      emptyText="No row conditions found"
    >
      {#snippet headerAction()}
        <PrimaryButton size="sm" onclick={openCreateDialog} class="shadow-sm">
          <Plus class="mr-2 h-4 w-4" />
          Define Access Rule
        </PrimaryButton>
      {/snippet}

      {#snippet cell({
        column,
        value,
        item
      }: {
        item: RowCondition;
        column: DataTableColumn;
        value: unknown;
      })}
        {#if column.key === 'roleName'}
          <div class="flex items-center gap-2">
            <Badge variant="outline" class="bg-primary/5 font-semibold text-primary">{item.roleName}</Badge>
          </div>
        {:else if column.key === 'resourceName'}
          <span class="text-sm font-medium text-foreground/80">{item.resourceName}</span>
        {:else if column.key === 'conditions'}
          {@const root = parseConditionJson(item.conditionJson)}
          {@const flatRules = flattenRules(root)}
          <div class="flex flex-wrap gap-1.5">
            {#each flatRules.slice(0, 2) as leaf (leaf.id)}
               <Chip class="h-6 px-2 text-[10px] bg-muted/50 border-muted-foreground/10">
                <span class="text-muted-foreground mr-1">{leaf.field}</span>
                <span class="font-bold text-foreground/70">{OPERATOR_LABELS[leaf.op] || leaf.op}</span>
                <span class="ml-1 italic opacity-70">"{leaf.value}"</span>
               </Chip>
            {/each}
            {#if flatRules.length > 2}
              <Badge variant="outline" class="h-6 text-[10px] text-muted-foreground border-dashed">
                +{flatRules.length - 2} more
              </Badge>
            {/if}
            {#if flatRules.length === 0}
              <span class="text-[10px] text-muted-foreground italic">No filters (View All)</span>
            {/if}
          </div>
        {:else if column.key === 'updatedAt'}
          <span class="text-xs">{new Date(value as string).toLocaleDateString()}</span>
        {:else if column.key === 'actions'}
          <div
            role="presentation"
            class="flex justify-end gap-1"
            onclick={(e) => e.stopPropagation()}
          >
            <IconButton variant="ghost" size="icon-sm" ariaLabel="Edit Access Rule" onclick={() => openEditDialog(item)}>
              <Pencil class="h-3.5 w-3.5" />
            </IconButton>
            <IconButton
              variant="ghost"
              size="icon-sm"
              class="text-destructive/60 hover:text-destructive hover:bg-destructive/10"
              ariaLabel="Delete Access Rule"
            >
              <Trash2 class="h-3.5 w-3.5" />
            </IconButton>
          </div>
        {:else}
          {String(value)}
        {/if}
      {/snippet}

      {#snippet expanded({ item }: { item: RowCondition })}
        {@const root = parseConditionJson(item.conditionJson)}
        <div class="bg-muted/10 p-6">
          <div class="flex flex-col gap-6">
            <div class="flex items-start gap-4">
              <div class="mt-1 rounded-full bg-primary/10 p-2 text-primary">
                <LayoutList class="h-5 w-5" />
              </div>
              <div class="space-y-1">
                <h4 class="text-sm font-bold uppercase tracking-wider text-muted-foreground">Access Logic Preview</h4>
                <p class="text-xl font-medium tracking-tight text-foreground/90 italic">
                  "{getSentence(item)}"
                </p>
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
               <div class="space-y-3 rounded-xl border bg-card p-4 shadow-sm">
                  <div class="flex items-center gap-2 text-xs font-bold uppercase tracking-widest text-muted-foreground">
                    <Info class="h-3.5 w-3.5" />
                    Internal Rule Set
                  </div>
                  <div class="space-y-2">
                    {#each flattenRules(root) as leaf (leaf.id)}
                      <div class="flex items-center justify-between rounded-lg bg-muted/30 px-3 py-2 text-sm border border-muted-foreground/5 transition-colors hover:bg-muted/50">
                        <span class="font-medium text-foreground/70">{leaf.field}</span>
                        <div class="flex items-center gap-2">
                          <Badge variant="outline" class="text-[10px] font-mono">{leaf.op}</Badge>
                          <span class="font-mono text-xs text-primary">{JSON.stringify(leaf.value)}</span>
                        </div>
                      </div>
                    {/each}
                  </div>
               </div>

               <div class="space-y-3 rounded-xl border bg-muted/20 p-4 border-dashed">
                  <div class="text-xs font-bold uppercase tracking-widest text-muted-foreground">Role Context</div>
                  <div class="grid gap-2">
                    <div class="flex justify-between text-sm">
                      <span class="text-muted-foreground">Assigned Role</span>
                      <span class="font-bold">{item.roleName}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                      <span class="text-muted-foreground">Target Resource</span>
                      <span class="font-medium">{item.resourceName}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                      <span class="text-muted-foreground">Rule ID</span>
                      <span class="font-mono text-xs opacity-60">{item.id}</span>
                    </div>
                  </div>
               </div>
            </div>
          </div>
        </div>
      {/snippet}
    </SmartTable>

    <RowConditionUpsertDialog bind:open={dialogOpen} initialData={editingCondition} />
  {/if}
</div>

