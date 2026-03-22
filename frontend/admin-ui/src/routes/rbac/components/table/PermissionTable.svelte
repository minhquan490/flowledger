<script lang="ts">
  import { SmartTable } from '$lib/components';
  import { 
    IconButton, 
    PrimaryButton, 
    ConfirmDialog,
    type DataTableColumn 
  } from '@medisphere/common-ui';
  import { Pencil, Trash2, Plus, LoaderCircle } from '@lucide/svelte';
  import { 
    usePermissionsQuery, 
    useDeletePermissionMutation 
  } from '../../hooks/usePermissions';
  import { toast } from 'svelte-sonner';
  import PermissionUpsertDialog from '../dialog/PermissionUpsertDialog.svelte';
  import type { Permission } from '../../types';

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
  
  let dialogOpen = $state(false);
  let editingPermission: Permission | undefined = $state(undefined);

  function openCreateDialog() {
    editingPermission = undefined;
    dialogOpen = true;
  }

  function openEditDialog(item: Permission) {
    editingPermission = item;
    dialogOpen = true;
  }

  // ── Delete ─────────────────────────────────────────────────────────────────
  const deleteMutation = useDeletePermissionMutation();
  let confirmDeleteOpen = $state(false);
  let permissionToDelete = $state<Permission | null>(null);
  let isDeleting = $state(false);

  function requestDelete(item: Permission) {
    permissionToDelete = item;
    confirmDeleteOpen = true;
  }

  async function handleDelete() {
    if (!permissionToDelete) return;
    isDeleting = true;
    try {
      await deleteMutation.mutateAsync(permissionToDelete.id);
      toast.success('Permission deleted successfully');
      confirmDeleteOpen = false;
      permissionToDelete = null;
    } catch {
      toast.error('Failed to delete permission. Please try again.');
    } finally {
      isDeleting = false;
    }
  }
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
        <PrimaryButton size="sm" onclick={openCreateDialog}>
          <Plus class="h-4 w-4" />
          Create Permission
        </PrimaryButton>
      {/snippet}
      {#snippet cell({ column, value, item }: { column: DataTableColumn; value: unknown; item: Permission })}
        {#if column.key === 'createdAt' || column.key === 'updatedAt'}
          {new Date(value as string).toLocaleDateString()}
        {:else if column.key === 'actions'}
          <div class="flex justify-end gap-2" role="presentation" onclick={(e) => e.stopPropagation()}>
            <IconButton variant="ghost" size="icon-sm" ariaLabel="Edit Permission" onclick={() => openEditDialog(item)}>
              <Pencil class="h-4 w-4" />
            </IconButton>
            <IconButton
              variant="ghost"
              size="icon-sm"
              class="text-destructive hover:text-destructive"
              ariaLabel="Delete Permission"
              onclick={() => requestDelete(item)}
              disabled={isDeleting && permissionToDelete?.id === item.id}
            >
              {#if isDeleting && permissionToDelete?.id === item.id}
                <LoaderCircle class="h-4 w-4 animate-spin" />
              {:else}
                <Trash2 class="h-4 w-4" />
              {/if}
            </IconButton>
          </div>
        {:else}
          {String(value)}
        {/if}
      {/snippet}
    </SmartTable>
    
    <PermissionUpsertDialog bind:open={dialogOpen} initialData={editingPermission} />

    <ConfirmDialog
      bind:open={confirmDeleteOpen}
      title="Delete Permission"
      description="Are you sure you want to delete this permission? This action cannot be undone."
      confirmText="Delete Permission"
      loading={isDeleting}
      onConfirm={handleDelete}
      onCancel={() => (permissionToDelete = null)}
    />
  {/if}
</div>
