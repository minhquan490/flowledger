import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Input } from "../../lib";

const meta = {
  title: "Components/Forms/Input",
  component: Input,
  tags: ["autodocs"],
  args: {
    label: "Full name",
    required: true,
    helperText: "Use your legal name",
    placeholder: "Jane Doe"
  }
} satisfies Meta<typeof Input>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Input formApi={form} name="fullName" label="Full name" required={true} helperText="Use your legal name" placeholder="Jane Doe" />`
      }
    }
  }
};
