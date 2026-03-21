<script lang="ts">
  import { SmartTable } from '$lib/components';
  import { IconButton, PrimaryButton, type DataTableColumn } from '@medisphere/common-ui';
  import { Pencil, Trash2, Plus } from '@lucide/svelte';
  import { usePermissionsQuery } from '../hooks/usePermissions';

  const columns: DataTableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'roleName', label: 'Role' },
    { key: 'resource', label: 'Resource' },
    { key: 'action', label: 'Action' },
    { key: 'isAllowed', label: 'Allowed' },
    { key: 'createdAt', label: 'Created At' },
    { key: 'updatedAt', label: 'Updated At' },
    { key: 'actions', label: 'Actions', class: 'w-[100px] text-center' }
  ];

  const permissionsQuery = usePermissionsQuery();
</script>

<div class="w-full">
  {#if permissionsQuery.isPending}
    <p>Loading permissions...</p>
  {:else if permissionsQuery.isError}
    <p class="text-red-500">Error loading permissions: {permissionsQuery.error.message}</p>
  {:else if permissionsQuery.data}
    <SmartTable
      {columns}
      data={permissionsQuery.data}
      emptyText="No permissions found"
      searchKeys={['roleId', 'resourceId', 'action']}
    >
      {#snippet headerAction()}
        <PrimaryButton size="sm">
          <Plus class="h-4 w-4" />
          Create Permission
        </PrimaryButton>
      {/snippet}
      {#snippet cell({ column, value }: { column: DataTableColumn; value: unknown })}
        {#if column.key === 'createdAt' || column.key === 'updatedAt'}
          {new Date(value as string).toLocaleDateString()}
        {:else if column.key === 'actions'}
          <div class="flex justify-end gap-2">
            <IconButton variant="ghost" size="icon-sm" ariaLabel="Edit Permission">
              <Pencil class="h-4 w-4" />
            </IconButton>
            <IconButton
              variant="ghost"
              size="icon-sm"
              class="text-destructive hover:text-destructive"
              ariaLabel="Delete Permission"
            >
              <Trash2 class="h-4 w-4" />
            </IconButton>
          </div>
        {:else}
          {String(value)}
        {/if}
      {/snippet}
    </SmartTable>
  {/if}
</div>
