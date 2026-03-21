import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PageHeader } from "../../lib";
import { textSnippet } from "../story-helpers";
import { Plus } from "@lucide/svelte";

// We need a wrapper to demonstrate the action snippet easily in Storybook.
// Since Svelte snippets are easier mapped with children in simple stories, we might use a custom snippet or render a raw HTML button.
const meta = {
  title: "Components/Layout/PageHeader",
  component: PageHeader,
  tags: ["autodocs"],
  args: {
    title: "Dashboard",
    subtitle: "Welcome back! Here is an overview of your operation.",
    breadcrumbs: [
      { label: "Home", href: "/" },
      { label: "Dashboard", href: "/dashboard" }
    ]
  },
  argTypes: {
    action: {
      control: "text",
      description: "Svelte Snippet for the action buttons area."
    }
  },
  parameters: {
    docs: {
      description: {
        component: "Standard page header with title, subtitle, breadcrumb, and optional actions."
      },
      source: {
        code: `<PageHeader title="Dashboard" subtitle="Welcome back!" breadcrumbs={[{ label: "Home", href: "/" }]} />`
      }
    }
  }
} satisfies Meta<typeof PageHeader>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};

export const WithoutSubtitle: Story = {
  args: {
    title: "Settings",
    subtitle: undefined,
    breadcrumbs: [
      { label: "Home", href: "/" },
      { label: "Settings", href: "/settings" }
    ]
  }
};

export const WithoutBreadcrumbs: Story = {
  args: {
    title: "Login",
    subtitle: "Please sign in to continue.",
    breadcrumbs: []
  }
};

import PageHeaderWithAction from "./PageHeaderWithAction.svelte";

export const WithActions: Story = {
  render: (args) => ({
    Component: PageHeaderWithAction,
    props: args
  }),
  args: {
    title: "Users",
    subtitle: "Manage your platform users and permissions here.",
    breadcrumbs: [
      { label: "Home", href: "/" },
      { label: "Settings", href: "/settings" },
      { label: "Users", href: "/settings/users" }
    ]
  },
  parameters: {
    docs: {
      source: {
        code: `<PageHeader title="Users" subtitle="Manage your platform users and permissions here." breadcrumbs={[...]} >
  {#snippet action()}
    <SecondaryButton><Download class="size-4 mr-2" /> Export</SecondaryButton>
    <PrimaryButton><Plus class="size-4 mr-2" /> Add User</PrimaryButton>
  {/snippet}
</PageHeader>`
      }
    }
  }
};
