import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Alert } from "../../lib";

const meta = {
  title: "Components/Feedback/Alert",
  component: Alert,
  tags: ["autodocs"],
  args: {
    title: "Attention",
    description: "Something needs your attention.",
    variant: "default"
  }
} satisfies Meta<typeof Alert>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Alert title=\"Attention\" description=\"Something needs your attention.\" variant=\"default\" />` } } }
};
