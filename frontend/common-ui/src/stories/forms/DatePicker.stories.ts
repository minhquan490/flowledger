import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { DatePicker } from "../../lib";

const meta = {
  title: "Components/Forms/DatePicker",
  component: DatePicker,
  tags: ["autodocs"],
  args: {
    placeholder: "Pick appointment date"
  }
} satisfies Meta<typeof DatePicker>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<DatePicker placeholder="Pick appointment date" />`
      }
    }
  }
};
