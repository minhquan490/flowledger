<script lang="ts">
  import { AppLayout } from "../../lib/components/layout";
  import { PrimaryButton } from "../../lib/components/buttons";
  import { LayoutDashboard, Users, Settings, BriefcaseMedical } from "@lucide/svelte";

  let sidebarOpen = $state(true);
</script>

<div class="h-[600px] w-full border rounded-lg overflow-hidden relative font-sans transform translate-x-0">
  <AppLayout
    appTitle="FlowLedger Admin"
    appLogo={BriefcaseMedical}
    pageTitle="Dashboard"
    pageSubtitle="Welcome back to the admin dashboard."
    bind:sidebarOpen
    breadcrumbs={[
      { label: "Home", href: "/" },
      { label: "Dashboard", href: "/dashboard" }
    ]}
    sidebarItems={[
      { id: "dashboard", label: "Dashboard", icon: LayoutDashboard, active: true },
      { id: "users", label: "Users", icon: Users },
      { id: "settings", label: "Settings", icon: Settings }
    ]}
  >
    {#snippet headerAction()}
      <PrimaryButton>Generate Report</PrimaryButton>
    {/snippet}

    <div class="p-6 bg-muted/10 min-h-full rounded-xl border-dashed border-2 m-1">
      <h2 class="text-xl font-semibold mb-4">Main Content Area</h2>
      <p class="text-muted-foreground">This is where your page content goes. The sidebar and header are provided by the AppLayout.</p>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
        {#each [1, 2, 3] as i}
          <div class="p-6 bg-background rounded-xl border shadow-sm">
            <h3 class="font-medium mb-2">Stat Card {i}</h3>
            <p class="text-3xl font-bold">1,234</p>
          </div>
        {/each}
      </div>
    </div>
  </AppLayout>
</div>
