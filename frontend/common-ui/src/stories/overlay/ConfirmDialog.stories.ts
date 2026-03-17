import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { ConfirmDialog } from "../../lib";

const meta = {
  title: "Components/Overlay/ConfirmDialog",
  component: ConfirmDialog,
  tags: ["autodocs"],
  args: {
    title: "Confirm removal",
    description: "Are you sure you want to remove this item?",
    confirmText: "Remove",
    cancelText: "Cancel"
  }
} satisfies Meta<typeof ConfirmDialog>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<ConfirmDialog title=\"Confirm removal\" description=\"Are you sure you want to remove this item?\" confirmText=\"Remove\" cancelText=\"Cancel\" />` } } }
};
