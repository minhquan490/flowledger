<script lang="ts">
  import { Tabs } from '@medisphere/common-ui';
  import RoleTable from './components/RoleTable.svelte';
  import ResourceTable from './components/ResourceTable.svelte';
  import PermissionTable from './components/PermissionTable.svelte';
  import RowConditionTable from './components/RowConditionTable.svelte';

  let selectedTab = $state('roles');
</script>

<div class="w-full">
  <Tabs
    bind:value={selectedTab}
    items={[
      { value: 'roles', label: 'Roles', content: rolesSnippet },
      { value: 'resources', label: 'Resources', content: resourcesSnippet },
      { value: 'permissions', label: 'Permissions', content: permissionsSnippet },
      { value: 'row-conditions', label: 'Row Conditions', content: rowConditionsSnippet }
    ]}
  />
</div>

{#snippet rolesSnippet()}
  <RoleTable />
{/snippet}

{#snippet resourcesSnippet()}
  <ResourceTable />
{/snippet}

{#snippet permissionsSnippet()}
  <PermissionTable />
{/snippet}

{#snippet rowConditionsSnippet()}
  <RowConditionTable />
{/snippet}
