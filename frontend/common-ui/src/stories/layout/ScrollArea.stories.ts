import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { ScrollArea } from "../../lib";

interface ScrollAreaStoryArgs {
  orientation?: "vertical" | "horizontal" | "both";
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/ScrollArea",
  component: ScrollArea,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Scrollable container wrapper composed from shadcn ScrollArea primitive."
      }
    }
  },
  render: (args) => ({
    Component: ScrollArea,
    props: args
  }),
  args: {
    orientation: "vertical",
    children: textSnippet(
      "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11\nLine 12"
    )
  },
  argTypes: {
    orientation: {
      control: "select",
      options: ["vertical", "horizontal", "both"]
    }
  }
} satisfies Meta<ScrollAreaStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<ScrollArea orientation="vertical">
  <div>Line 1</div>
  <div>Line 2</div>
  <div>Line 3</div>
  <div>Line 4</div>
  <div>Line 5</div>
</ScrollArea>`
      }
    }
  }
};
