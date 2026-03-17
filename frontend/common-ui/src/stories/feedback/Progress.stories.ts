import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Progress } from "../../lib";

const meta = {
  title: "Components/Feedback/Progress",
  component: Progress,
  tags: ["autodocs"],
  args: {
    value: 64,
    max: 100
  }
} satisfies Meta<typeof Progress>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Progress value={64} max={100} />` } } }
};
