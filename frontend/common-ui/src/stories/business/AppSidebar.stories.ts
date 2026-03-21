import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AppSidebar } from "../../lib";
import { textSnippet } from "../story-helpers";
import { Home, Users, Calendar, BarChart, Settings, Activity } from "@lucide/svelte";

const meta = {
  title: "Components/Business/AppSidebar",
  component: AppSidebar,
  tags: ["autodocs"],
  args: {
    title: "Business Dashboard",
    logo: Activity,
    open: true,
    items: [
      { id: "home", label: "Home", href: "/", active: true, icon: Home },
      { id: "patients", label: "Patients", href: "/patients", icon: Users },
      { id: "appointments", label: "Appointments", href: "/appointments", icon: Calendar },
      { id: "reports", label: "Reports", href: "/reports", icon: BarChart },
      { id: "settings", label: "Settings", href: "/settings", icon: Settings }
    ],
    children: textSnippet("Page Content goes here...")
  },
  parameters: {
    docs: {
      description: {
        component: "Application sidebar specifically tailored for business UI, wrapping the common-ui SidebarShell."
      },
      source: {
        code: `<AppSidebar title="Business Dashboard" logo={Activity} items={[{ id: "home", label: "Home", href: "/", icon: Home }]} />`
      }
    }
  }
} satisfies Meta<typeof AppSidebar>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};

export const WithManyItems: Story = {
  args: {
    title: "Admin Panel",
    logo: Activity,
    items: [
      { id: "dashboard", label: "Dashboard", href: "/admin", active: true, icon: Home },
      { id: "users", label: "Users Management", href: "/admin/users", icon: Users },
      { id: "roles", label: "Roles & Permissions", href: "/admin/roles", icon: Users },
      { id: "audit", label: "Audit Logs", href: "/admin/audit", icon: BarChart },
      { id: "system", label: "System Health", href: "/admin/health", icon: Activity },
      { id: "settings", label: "Settings", href: "/admin/settings", icon: Settings }
    ]
  }
};
