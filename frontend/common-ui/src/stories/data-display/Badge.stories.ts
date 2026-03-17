import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Badge } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Badge",
  component: Badge,
  tags: ["autodocs"],
  args: {
    variant: "default",
    children: textSnippet("Active")
  }
} satisfies Meta<typeof Badge>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Badge variant="default">Active</Badge>` } } }
};
