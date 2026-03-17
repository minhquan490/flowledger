<script lang="ts">
  import { Tabs } from '@medisphere/common-ui';
  import RoleTable from './components/RoleTable.svelte';
  import ResourceTable from './components/ResourceTable.svelte';

  let selectedTab = $state('roles');
</script>

<div class="container mx-auto py-8">
  <div class="mb-8 flex items-center justify-between">
    <h1 class="text-3xl font-bold tracking-tight">Role-Based Access Control</h1>
    <p class="text-muted-foreground">Manage roles, permissions, and resources.</p>
  </div>

  <Tabs
    bind:value={selectedTab}
    items={[
      { value: 'roles', label: 'Roles', content: rolesSnippet },
      { value: 'resources', label: 'Resources', content: resourcesSnippet }
    ]}
  />
</div>

{#snippet rolesSnippet()}
  <div class="mt-4">
    <RoleTable />
  </div>
{/snippet}

{#snippet resourcesSnippet()}
  <div class="mt-4">
    <ResourceTable />
  </div>
{/snippet}

