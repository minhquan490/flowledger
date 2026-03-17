import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Drawer } from "../../lib";

const meta = {
  title: "Components/Overlay/Drawer",
  component: Drawer,
  tags: ["autodocs"],
  args: {
    side: "left",
    title: "Navigation",
    description: "Quick navigation drawer"
  }
} satisfies Meta<typeof Drawer>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Drawer side=\"left\" title=\"Navigation\" description=\"Quick navigation drawer\" />` } } }
};
