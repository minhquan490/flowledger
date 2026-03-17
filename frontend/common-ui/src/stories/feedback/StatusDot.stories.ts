import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { StatusDot } from "../../lib";

const meta = {
  title: "Components/Feedback/StatusDot",
  component: StatusDot,
  tags: ["autodocs"],
  args: {
    status: "success",
    pulse: true,
    showLabel: true
  }
} satisfies Meta<typeof StatusDot>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<StatusDot status=\"success\" pulse={true} showLabel={true} />` } } }
};
