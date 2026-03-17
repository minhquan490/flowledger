import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Tooltip } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Tooltip",
  component: Tooltip,
  tags: ["autodocs"],
  args: {
    content: "More information",
    children: textSnippet("Hover me")
  }
} satisfies Meta<typeof Tooltip>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Tooltip content="More information">Hover me</Tooltip>` } } }
};
