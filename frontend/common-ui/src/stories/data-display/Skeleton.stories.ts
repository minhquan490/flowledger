import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Skeleton } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Skeleton",
  component: Skeleton,
  tags: ["autodocs"],
  args: {
    class: "h-4 w-48"
  }
} satisfies Meta<typeof Skeleton>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Skeleton class="h-4 w-48" />` } } }
};
