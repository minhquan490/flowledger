import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Switch } from "../../lib";

const meta = {
  title: "Components/Forms/Switch",
  component: Switch,
  tags: ["autodocs"],
  args: {
    label: "Enable notifications",
    checked: true
  }
} satisfies Meta<typeof Switch>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Switch label="Enable notifications" checked={true} />`
      }
    }
  }
};
