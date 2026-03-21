<script lang="ts">
  import { useRowConditionsQuery } from '../hooks/useRowConditions';
  import {
    Table,
    IconButton,
    PrimaryButton,
    type DataTableColumn
  } from '@medisphere/common-ui';
  import { SmartTable } from '$lib/components';
  import { Pencil, Trash2, Plus } from '@lucide/svelte';
  import type { RowCondition } from '../types';

  const rowConditionsQuery = useRowConditionsQuery();

  const columns: DataTableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'roleName', label: 'Role' },
    { key: 'resourceName', label: 'Resource' },
    { key: 'createdAt', label: 'Created At' },
    { key: 'updatedAt', label: 'Updated At' },
    { key: 'actions', label: 'Actions', class: 'w-[100px] text-center' }
  ];

  type ParsedCondition = {
    field: string;
    op: string;
    value: string;
  };
  const OPERATOR_LABELS: Record<string, string> = {
    eq: 'Equals',
    ne: 'Not Equals',
    neq: 'Not Equals',
    gt: 'Greater Than',
    ge: 'Greater or Equals',
    gte: 'Greater or Equals',
    lt: 'Less Than',
    le: 'Less or Equals',
    lte: 'Less or Equals',
    between: 'Between',
    notbetween: 'Not Between',
    nbetween: 'Not Between',
    in: 'In',
    notin: 'Not In',
    nin: 'Not In',
    like: 'Like',
    notlike: 'Not Like',
    ilike: 'Case-Insensitive Like',
    notilike: 'Not Case-Insensitive Like',
    isnull: 'Is Null',
    isnotnull: 'Is Not Null'
  };

  function formatConditionValue(op: string, value: unknown): string {
    if (value == null || value === '') return '';
    
    const normalizedOp = op.toLowerCase();
    
    // Handle BETWEEN
    if (['between', 'notbetween', 'nbetween'].includes(normalizedOp)) {
      if (Array.isArray(value) && value.length >= 2) {
        return `${value[0]} and ${value[1]}`;
      } else if (typeof value === 'object' && value !== null) {
        const v = value as Record<string, unknown>;
        if ('from' in v && 'to' in v) {
          return `${v.from} and ${v.to}`;
        }
      }
    }
    
    // Handle IN
    if (['in', 'notin', 'nin'].includes(normalizedOp)) {
      if (Array.isArray(value)) {
        return value.join(', ');
      }
    }
    
    // Fallback for other objects
    if (typeof value === 'object') {
      return JSON.stringify(value);
    }
    
    return String(value);
  }

  function parseConditionJson(jsonString: string): ParsedCondition[] {
    try {
      const parsed = JSON.parse(jsonString) as Record<string, { op?: string; value?: unknown }>;
      return Object.entries(parsed).map(([field, data]) => {
        const rawOp = data?.op || '';
        const humanOp = OPERATOR_LABELS[rawOp.toLowerCase()] || rawOp;
        return {
          field,
          op: humanOp,
          value: formatConditionValue(rawOp, data?.value)
        };
      });
    } catch (e) {
      console.error('Failed to parse condition JSON', e);
      return [];
    }
  }

</script>

<div>
  {#if rowConditionsQuery.isPending}
    <p>Loading row conditions...</p>
  {:else if rowConditionsQuery.isError}
    <p class="text-red-500">Error loading row conditions: {rowConditionsQuery.error.message}</p>
  {:else if rowConditionsQuery.data}
    <SmartTable 
      {columns} 
      data={rowConditionsQuery.data} 
      rowKey="id"
      searchKeys={['roleName', 'resourceName']}
      emptyText="No row conditions found"
    >
      {#snippet headerAction()}
        <PrimaryButton size="sm">
          <Plus class="h-4 w-4" />
          Create Row Condition
        </PrimaryButton>
      {/snippet}
      {#snippet cell({ column, value }: { item: RowCondition; column: DataTableColumn; value: unknown })}
        {#if column.key === 'createdAt' || column.key === 'updatedAt'}
          {new Date(value as string).toLocaleDateString()}
        {:else if column.key === 'actions'}
          <div role="presentation" class="flex justify-end gap-2" onclick={(e) => e.stopPropagation()}>
            <IconButton variant="ghost" size="icon-sm" ariaLabel="Edit Row Condition">
              <Pencil class="h-4 w-4" />
            </IconButton>
            <IconButton
              variant="ghost"
              size="icon-sm"
              class="text-destructive hover:text-destructive"
              ariaLabel="Delete Row Condition"
            >
              <Trash2 class="h-4 w-4" />
            </IconButton>
          </div>
        {:else}
          {String(value)}
        {/if}
      {/snippet}

      {#snippet expanded({ item }: { item: RowCondition })}
        <div class="p-4">
          <h4 class="mb-4 text-sm font-medium">Row Conditions</h4>
          <div class="rounded-md border bg-background">
            <Table 
              headers={['Field', 'Operator', 'Value']}
              rows={parseConditionJson(item.conditionJson).map((c) => [c.field, c.op, c.value])}
              emptyText="No conditions defined"
            />
          </div>
        </div>
      {/snippet}
    </SmartTable>
  {/if}
</div>
