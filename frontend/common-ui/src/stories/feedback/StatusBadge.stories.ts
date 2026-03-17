import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { StatusBadge } from "../../lib";

const meta = {
  title: "Components/Feedback/StatusBadge",
  component: StatusBadge,
  tags: ["autodocs"],
  args: {
    label: "In review",
    status: "warning"
  }
} satisfies Meta<typeof StatusBadge>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<StatusBadge label=\"In review\" status=\"warning\" />` } } }
};
