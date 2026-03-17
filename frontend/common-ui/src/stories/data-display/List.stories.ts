import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { List } from "../../lib";

const meta = {
  title: "Components/DataDisplay/List",
  component: List,
  tags: ["autodocs"],
  args: {
    items: ["First", "Second", "Third"]
  }
} satisfies Meta<typeof List>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<List items={["First", "Second"]} />` } } }
};
