import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { ErrorText } from "../../lib";

const meta = {
  title: "Components/Forms/ErrorText",
  component: ErrorText,
  tags: ["autodocs"],
  args: {
    children: textSnippet("This field is required")
  }
} satisfies Meta<typeof ErrorText>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<ErrorText>This field is required</ErrorText>`
      }
    }
  }
};
