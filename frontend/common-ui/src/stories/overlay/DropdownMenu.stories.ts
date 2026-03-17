import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { DropdownMenu } from "../../lib";

const meta = {
  title: "Components/Overlay/DropdownMenu",
  component: DropdownMenu,
  tags: ["autodocs"],
  args: {
    items: [{ label: "Edit" }, { label: "Duplicate" }, { label: "Archive" }]
  }
} satisfies Meta<typeof DropdownMenu>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<DropdownMenu items={[{ label: 'Edit' }, { label: 'Duplicate' }, { label: 'Archive' }]} />` } } }
};
