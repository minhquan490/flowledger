<script lang="ts">
  import { DataTable } from '@medisphere/common-ui';
  import { executeSearch } from '$lib/api/graphql';
  import { onMount } from 'svelte';

  let resources: any[] = $state([]);
  let loading = $state(true);
  let error = $state('');

  const columns: { key: string; label: string }[] = [
    { key: 'id', label: 'ID' },
    { key: 'name', label: 'Name' },
    { key: 'description', label: 'Description' },
    { key: 'isSystemManaged', label: 'System Managed' }
  ];

  onMount(async () => {
    // Mock data for Resources
    resources = [
      { id: "1", name: "Users", description: "Manage system users", isSystemManaged: true },
      { id: "2", name: "Roles", description: "Manage RBAC roles", isSystemManaged: true },
      { id: "3", name: "Settings", description: "System configuration", isSystemManaged: false }
    ];
    loading = false;
  });
</script>

<div>
  {#if loading}
    <p>Loading resources...</p>
  {:else if error}
    <p class="text-red-500">Error loading resources: {error}</p>
  {:else}
    <DataTable {columns} rows={resources} emptyText="No resources found" />
  {/if}
</div>
