import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { EmptyState } from "../../lib";

const meta = {
  title: "Components/DataDisplay/EmptyState",
  component: EmptyState,
  tags: ["autodocs"],
  args: {
    title: "No records found",
    description: "Try adjusting your filters"
  }
} satisfies Meta<typeof EmptyState>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `< EmptyState title = "No records found" description = "Try adjusting your filters" /> ` } } }
};
