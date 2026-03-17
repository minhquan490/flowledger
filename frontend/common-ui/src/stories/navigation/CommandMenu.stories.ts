import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { CommandMenu } from "../../lib";

const meta = {
  title: "Components/Navigation/CommandMenu",
  component: CommandMenu,
  tags: ["autodocs"],
  args: {
    open: true,
    items: [
      { value: "new-patient", label: "New patient", shortcut: "N" },
      { value: "search-patient", label: "Search patient", shortcut: "S" },
      { value: "open-settings", label: "Open settings", shortcut: "G" }
    ]
  },
  parameters: {
    docs: {
      description: {
        component: "Command menu/palette wrapper composed from shadcn Command + Dialog primitives."
      },
      source: {
        code: `<CommandMenu open={true} items={[{ value: "search", label: "Search" }]} />`
      }
    }
  }
} satisfies Meta<typeof CommandMenu>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
