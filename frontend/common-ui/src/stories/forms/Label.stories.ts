import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Label } from "../../lib";

const meta = {
  title: "Components/Forms/Label",
  component: Label,
  tags: ["autodocs"],
  args: {
    required: true,
    children: textSnippet("Patient name")
  }
} satisfies Meta<typeof Label>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Label required={true}>Patient name</Label>`
      }
    }
  }
};
