import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Toast } from "../../lib";

const meta = {
  title: "Components/Feedback/Toast",
  component: Toast,
  tags: ["autodocs"],
  args: {
    showDemoButton: true,
    demoLabel: "Show toast",
    demoMessage: "Saved successfully"
  }
} satisfies Meta<typeof Toast>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Toast showDemoButton={true} demoLabel=\"Show toast\" demoMessage=\"Saved successfully\" />` } } }
};
