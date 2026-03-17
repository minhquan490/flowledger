import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { HelperText } from "../../lib";

const meta = {
  title: "Components/Forms/HelperText",
  component: HelperText,
  tags: ["autodocs"],
  args: {
    children: textSnippet("This field is optional")
  }
} satisfies Meta<typeof HelperText>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `< HelperText > This field is optional </HelperText>`
      }
    }
  }
};
