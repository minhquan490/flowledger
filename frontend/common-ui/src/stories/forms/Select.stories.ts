import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Select } from "../../lib";

const meta = {
  title: "Components/Forms/Select",
  component: Select,
  tags: ["autodocs"],
  args: {
    placeholder: "Choose status",
    options: [
      { value: "active", label: "Active" },
      { value: "inactive", label: "Inactive" }
    ]
  }
} satisfies Meta<typeof Select>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Select placeholder="Choose status" options={[{ value: "active", label: "Active" }, { value: "inactive", label: "Inactive" }]} />`
      }
    }
  }
};
