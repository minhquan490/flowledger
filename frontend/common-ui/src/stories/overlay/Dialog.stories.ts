import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Dialog } from "../../lib";

const meta = {
  title: "Components/Overlay/Dialog",
  component: Dialog,
  tags: ["autodocs"],
  args: {
    title: "Delete record",
    description: "This action cannot be undone."
  }
} satisfies Meta<typeof Dialog>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Dialog title=\"Delete record\" description=\"This action cannot be undone.\" />` } } }
};
