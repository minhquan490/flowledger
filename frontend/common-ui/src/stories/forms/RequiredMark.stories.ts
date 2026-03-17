import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { RequiredMark } from "../../lib";

const meta = {
  title: "Components/Forms/RequiredMark",
  component: RequiredMark,
  tags: ["autodocs"]
} satisfies Meta<typeof RequiredMark>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<RequiredMark />`
      }
    }
  }
};
