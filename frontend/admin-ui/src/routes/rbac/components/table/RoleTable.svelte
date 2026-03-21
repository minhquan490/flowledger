<script lang="ts">
  import { SmartTable } from '$lib/components';
  import { IconButton, PrimaryButton, type DataTableColumn } from '@medisphere/common-ui';
  import { Pencil, Trash2, Plus } from '@lucide/svelte';
  import { useRolesQuery } from '../../hooks/useRoles';
  import RoleUpsertDialog from '../dialog/RoleUpsertDialog.svelte';
  import type { Role } from '../../types';

  const rolesQuery = useRolesQuery();

  const columns: DataTableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'code', label: 'Code' },
    { key: 'name', label: 'Name' },
    { key: 'isDefaultRole', label: 'Default' },
    { key: 'createdAt', label: 'Created At' },
    { key: 'updatedAt', label: 'Updated At' },
    { key: 'actions', label: 'Actions', class: 'w-[100px] text-center' }
  ];

  let dialogOpen = $state(false);
  let editingRole: Role | undefined = $state(undefined);

  function openCreateDialog() {
    editingRole = undefined;
    dialogOpen = true;
  }

  function openEditDialog(item: Role) {
    editingRole = item;
    dialogOpen = true;
  }
</script>

<div class="w-full">
  {#if rolesQuery.isPending}
    <p>Loading roles...</p>
  {:else if rolesQuery.isError}
    <p class="text-red-500">Error loading roles: {rolesQuery.error.message}</p>
  {:else if rolesQuery.data}
    <SmartTable
      {columns}
      data={rolesQuery.data}
      emptyText="No roles found"
      searchKeys={['code', 'name']}
    >
      {#snippet headerAction()}
        <PrimaryButton size="sm" onclick={openCreateDialog}>
          <Plus class="h-4 w-4" />
          Create Role
        </PrimaryButton>
      {/snippet}
      {#snippet cell({ column, value, item }: { column: DataTableColumn; value: unknown; item: Role })}
        {#if column.key === 'createdAt' || column.key === 'updatedAt'}
          {new Date(value as string).toLocaleDateString()}
        {:else if column.key === 'actions'}
          <div class="flex justify-end gap-2" role="presentation" onclick={(e) => e.stopPropagation()}>
            <IconButton variant="ghost" size="icon-sm" ariaLabel="Edit Role" onclick={() => openEditDialog(item)}>
              <Pencil class="h-4 w-4" />
            </IconButton>
            <IconButton
              variant="ghost"
              size="icon-sm"
              class="text-destructive hover:text-destructive"
              ariaLabel="Delete Role"
            >
              <Trash2 class="h-4 w-4" />
            </IconButton>
          </div>
        {:else}
          {value as string}
        {/if}
      {/snippet}
    </SmartTable>
    
    <RoleUpsertDialog bind:open={dialogOpen} initialData={editingRole} />
  {/if}
</div>
