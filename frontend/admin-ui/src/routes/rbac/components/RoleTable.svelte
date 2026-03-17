<script lang="ts">
  import { DataTable } from "@medisphere/common-ui";
  import type { DataTableColumn } from "@medisphere/common-ui/components/data-display/DataTable.svelte";
  import { executeSearch } from "$lib/api/graphql";
  import { onMount } from "svelte";

  let roles: any[] = $state([]);
  let loading = $state(true);
  let error = $state("");

  const columns: DataTableColumn[] = [
    { key: "id", label: "ID" },
    { key: "code", label: "Code" },
    { key: "name", label: "Name" },
    { key: "isDefaultRole", label: "Default" },
    { key: "isSystemManaged", label: "System Managed" }
  ];

  onMount(async () => {
    // Mock data for Roles
    roles = [
      { id: "1", code: "SUPER_ADMIN", name: "Super Administrator", isDefaultRole: false, isSystemManaged: true },
      { id: "2", code: "USER", name: "Standard User", isDefaultRole: true, isSystemManaged: true },
      { id: "3", code: "EDITOR", name: "Content Editor", isDefaultRole: false, isSystemManaged: false }
    ];
    loading = false;
  });

</script>

<div>
  {#if loading}
    <p>Loading roles...</p>
  {:else if error}
    <p class="text-red-500">Error loading roles: {error}</p>
  {:else}
    <DataTable {columns} rows={roles} emptyText="No roles found" />
  {/if}
</div>
