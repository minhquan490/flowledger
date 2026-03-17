# Plan: RBAC Management UI in `admin-ui`

This plan outlines the steps to initialize `shadcn-svelte` in the `admin-ui` project and create the necessary pages and components for managing the RBAC feature.

## Proposed Changes

### 1. shadcn-svelte Integration - Done

We will initialize `shadcn-svelte` in `admin-ui`.
Since it's a SvelteKit project, we need to run `bunx mrm@latest shadcn-svelte@latest init` (or the equivalent shadcn-svelte init command) to setup the `components.json` and basic configurations if it hasn't been done already. Wait, the prompt says "Before that add shadcn to admin-ui". I will use the standard commands.

### 2. RBAC Management Pages

We will create a multi-tab interface under `src/routes/(admin)/rbac` or similar to manage Roles and Resources.

#### [NEW] [src/routes/rbac/+page.svelte](file:///home/quan/projects/FlowLedger/frontend/admin-ui/src/routes/rbac/+page.svelte)
The main container page for RBAC. It will use the `Tabs` component from `common-ui` (or shadcn) to switch between "Roles" and "Resources".

#### [NEW] [src/routes/rbac/roles/+page.svelte](file:///home/quan/projects/FlowLedger/frontend/admin-ui/src/routes/rbac/roles/+page.svelte)
(or as components inside the main page). Let's implement them as components within `src/routes/rbac/components/`.

- **RoleTable.svelte**: Displays a list of [RbacRole](file:///home/quan/projects/FlowLedger/platform/rbac/src/main/java/io/flowledger/platform/rbac/domain/role/view/RbacRoleView.java#13-66) objects (id, code, name, isDefaultRole, isSystemManaged).
- **ResourceTable.svelte**: Displays a list of [RbacResource](file:///home/quan/projects/FlowLedger/platform/rbac/src/main/java/io/flowledger/platform/rbac/domain/resource/view/RbacResourceView.java#13-59) objects (id, name, description, isSystemManaged).

### 3. API Integration (GraphQL)

We will use the backend's generic GraphQL API to fetch the data. The backend uses a dynamic schema based on the `@GraphQlModel` annotations (e.g., `model: "rbacRole"`, `model: "rbacResource"`).

We will define a generic, shared GraphQL API utility/hook (e.g., `src/lib/api/graphql.ts`) to handle all [read](file:///home/quan/projects/FlowLedger/platform/graphql/src/main/java/io/flowledger/platform/graphql/infrastructure/web/GraphQlQueryController.java#58-68), [search](file:///home/quan/projects/FlowLedger/platform/graphql/src/main/java/io/flowledger/platform/graphql/infrastructure/web/GraphQlQueryController.java#69-79), and [mutate](file:///home/quan/projects/FlowLedger/platform/graphql/src/main/java/io/flowledger/platform/graphql/infrastructure/web/GraphQlQueryController.java#80-90) operations. Since the backend query structure is identical across all entities (only changing the `model` parameter), this shared approach will prevent duplication and standardize data fetching.

Pages and components will use this shared utility to execute generic [search](file:///home/quan/projects/FlowLedger/platform/graphql/src/main/java/io/flowledger/platform/graphql/infrastructure/web/GraphQlQueryController.java#69-79) queries like:
```graphql
query SearchRoles {
  search(request: {
    model: "rbacRole",
    page: { offset: 0, limit: 50 },
    fields: ["id", "code", "name", "isDefaultRole", "isSystemManaged", "createdAt"]
  }) {
    items
    total
  }
}
```
Similarly for `rbacResource` fetching properties like `id`, `name`, `description`, [isSystemManaged](file:///home/quan/projects/FlowLedger/platform/rbac/src/main/java/io/flowledger/platform/rbac/domain/role/view/RbacRoleResourcePermissionView.java#53-59).

## Verification

### Automated Tests
- N/A

### Manual Verification
- Start the server (`bun run dev`) and navigate to `/rbac`.
- Verify that the layout loads, displaying tables with dummy or fetched data.
- Verify that the UI uses components from `common-ui`.
