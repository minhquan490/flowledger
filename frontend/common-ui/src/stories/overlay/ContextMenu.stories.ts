import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { ContextMenu } from "../../lib";

const meta = {
  title: "Components/Overlay/ContextMenu",
  component: ContextMenu,
  tags: ["autodocs"],
  args: {
    areaLabel: "Right-click this block",
    items: [{ label: "Open" }, { label: "Rename" }, { label: "Delete" }]
  }
} satisfies Meta<typeof ContextMenu>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<ContextMenu areaLabel=\"Right-click this block\" items={[{ label: 'Open' }, { label: 'Rename' }, { label: 'Delete' }]} />` } } }
};
