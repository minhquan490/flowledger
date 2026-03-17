import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Textarea } from "../../lib";

const meta = {
  title: "Components/Forms/Textarea",
  component: Textarea,
  tags: ["autodocs"],
  args: { placeholder: "Write your note" }
} satisfies Meta<typeof Textarea>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Textarea placeholder="Write your note" />`
      }
    }
  }
};
