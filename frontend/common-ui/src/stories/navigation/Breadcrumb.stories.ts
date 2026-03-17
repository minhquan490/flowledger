import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Breadcrumb } from "../../lib";

const meta = {
  title: "Components/Navigation/Breadcrumb",
  component: Breadcrumb,
  tags: ["autodocs"],
  args: {
    items: [
      { label: "Home", href: "/" },
      { label: "Patients", href: "/patients" },
      { label: "Profile" }
    ]
  },
  parameters: {
    docs: {
      description: {
        component: "Breadcrumb navigation wrapper composed from shadcn Breadcrumb primitives."
      },
      source: {
        code: `<Breadcrumb items={[{ label: "Home", href: "/" }, { label: "Profile" }]} />`
      }
    }
  }
} satisfies Meta<typeof Breadcrumb>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
