import type { Meta, StoryObj } from "@storybook/svelte-vite";
import AppLayoutDemo from "./AppLayoutDemo.svelte";

const meta = {
  title: "Components/Layout/AppLayout",
  component: AppLayoutDemo,
  tags: ["autodocs"],
  parameters: {
    layout: "padded",
    docs: {
      description: {
        component: "A high-level application layout combining the AppSidebar, PageHeader, and a main content area."
      },
      source: {
        code: `<script lang="ts">
  import { AppLayout } from "@medisphere/common-ui/components/layout";
  import { LayoutDashboard, Users, Settings, BriefcaseMedical } from "@lucide/svelte";

  let sidebarOpen = $state(true);
</script>

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

  <div class="p-6">
    <h2 class="text-xl font-semibold mb-4">Main Content Area</h2>
    <p>This is where your page content goes.</p>
  </div>
</AppLayout>`
      }
    }
  }
} satisfies Meta<typeof AppLayoutDemo>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
