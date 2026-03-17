import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PhoneInput } from "../../lib";
import PhoneInputDemo from "./PhoneInputDemo.svelte";

const meta = {
  title: "Components/Forms/PhoneInput",
  component: PhoneInputDemo,
  tags: ["autodocs"],
  args: {
    label: "Phone",
    helperText: "Include country code",
    placeholder: "+1 (555) 123-4567"
  }
} satisfies Meta<typeof PhoneInput>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<PhoneInput formApi={form} name="phone" label="Phone" helperText="Include country code" placeholder="+1 (555) 123-4567" />`
      }
    }
  }
};
