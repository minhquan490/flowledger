import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Avatar } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Avatar",
  component: Avatar,
  tags: ["autodocs"],
  args: {
    fallback: "JD"
  }
} satisfies Meta<typeof Avatar>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Avatar fallback="JD" />` } } }
};
