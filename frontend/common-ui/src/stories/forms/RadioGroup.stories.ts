import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { RadioGroup } from "../../lib";

const meta = {
  title: "Components/Forms/RadioGroup",
  component: RadioGroup,
  tags: ["autodocs"],
  args: {
    options: [
      { value: "male", label: "Male" },
      { value: "female", label: "Female" }
    ],
    value: "male"
  }
} satisfies Meta<typeof RadioGroup>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<RadioGroup value="male" options={[{ value: "male", label: "Male" }, { value: "female", label: "Female" }]} />`
      }
    }
  }
};
