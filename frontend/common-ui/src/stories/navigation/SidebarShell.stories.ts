import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { SidebarShell } from "../../lib";

const meta = {
  title: "Components/Navigation/SidebarShell",
  component: SidebarShell,
  tags: ["autodocs"],
  args: {
    title: "Admin",
    open: true,
    items: [
      { id: "dashboard", label: "Dashboard", active: true },
      { id: "patients", label: "Patients" },
      { id: "appointments", label: "Appointments" },
      { id: "settings", label: "Settings" }
    ],
    children: textSnippet("Main content area")
  },
  parameters: {
    docs: {
      description: {
        component: "Generic app shell with collapsible sidebar and content area, composed from shadcn Sidebar primitives."
      },
      source: {
        code: `<SidebarShell title="Admin" items={[{ id: "dashboard", label: "Dashboard" }]} />`
      }
    }
  }
} satisfies Meta<typeof SidebarShell>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
