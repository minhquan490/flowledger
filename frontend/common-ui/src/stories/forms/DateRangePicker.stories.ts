import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { DateRangePicker } from "../../lib";

const meta = {
  title: "Components/Forms/DateRangePicker",
  component: DateRangePicker,
  tags: ["autodocs"],
  args: {
    startPlaceholder: "From",
    endPlaceholder: "To"
  }
} satisfies Meta<typeof DateRangePicker>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `< DateRangePicker startPlaceholder = "From" endPlaceholder = "To" /> `
      }
    }
  }
};
