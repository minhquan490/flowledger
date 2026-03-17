import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Checkbox } from "../../lib";

const meta = {
  title: "Components/Forms/Checkbox",
  component: Checkbox,
  tags: ["autodocs"],
  args: {
    label: "Accept terms",
    description: "You agree to the policy"
  }
} satisfies Meta<typeof Checkbox>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Checkbox label="Accept terms" description="You agree to the policy" />`
      }
    }
  }
};
