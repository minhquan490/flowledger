import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Chip } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Chip",
  component: Chip,
  tags: ["autodocs"],
  args: {
    children: textSnippet("Inpatient")
  }
} satisfies Meta<typeof Chip>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Chip>Inpatient</Chip>` } } }
};
