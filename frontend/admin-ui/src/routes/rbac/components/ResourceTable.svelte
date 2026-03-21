<script lang="ts">
  import { useResourcesQuery } from '../hooks/useResources';
  import {
    Table,
    type DataTableColumn
  } from '@medisphere/common-ui';
  import { SmartTable } from '$lib/components';
  import type { Resource, ResourceField } from '../types';

  const resourcesQuery = useResourcesQuery();

  const columns: DataTableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'name', label: 'Name' },
    { key: 'description', label: 'Description' },
    { key: 'createdAt', label: 'Created At' },
    { key: 'updatedAt', label: 'Updated At' }
  ];
</script>

<div>
  {#if resourcesQuery.isPending}
    <p>Loading resources...</p>
  {:else if resourcesQuery.isError}
    <p class="text-red-500">Error loading resources: {resourcesQuery.error.message}</p>
  {:else if resourcesQuery.data}
    <SmartTable 
      {columns} 
      data={resourcesQuery.data} 
      rowKey="id"
      searchKeys={['name', 'description']}
    >
      {#snippet cell({ column, value }: { item: Resource; column: DataTableColumn; value: unknown })}
        {#if column.key === 'createdAt' || column.key === 'updatedAt'}
          {new Date(value as string).toLocaleDateString()}
        {:else}
          {value as string}
        {/if}
      {/snippet}

      {#snippet expanded({ item }: { item: Resource })}
        <div class="p-4">
          <h4 class="mb-4 text-sm font-medium">Resource Fields</h4>
          <div class="rounded-md border bg-background">
            <Table 
              headers={['Field Name', 'Source Method']}
              rows={item.fields.map((f: ResourceField) => [f.fieldName, f.sourceMethodName])}
              emptyText="No fields defined"
            />
          </div>
        </div>
      {/snippet}
    </SmartTable>
  {/if}
</div>
