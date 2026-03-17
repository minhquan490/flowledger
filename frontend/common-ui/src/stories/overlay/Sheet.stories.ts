import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Sheet } from "../../lib";

const meta = {
  title: "Components/Overlay/Sheet",
  component: Sheet,
  tags: ["autodocs"],
  args: {
    side: "right",
    title: "Filters",
    description: "Adjust filters"
  }
} satisfies Meta<typeof Sheet>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Sheet side=\"right\" title=\"Filters\" description=\"Adjust filters\" />` } } }
};
