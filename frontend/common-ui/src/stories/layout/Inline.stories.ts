import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { Inline } from "../../lib";

interface InlineStoryArgs {
  gap?: "none" | "xs" | "sm" | "md" | "lg" | "xl";
  align?: "start" | "center" | "end" | "stretch" | "baseline";
  justify?: "start" | "center" | "end" | "between";
  wrap?: boolean;
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/Inline",
  component: Inline,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Horizontal layout primitive for inline grouping with spacing and wrapping controls."
      }
    }
  },
  render: (args) => ({
    Component: Inline,
    props: args
  }),
  args: {
    gap: "md",
    align: "center",
    justify: "start",
    wrap: false,
    children: textSnippet("Item A\nItem B\nItem C")
  },
  argTypes: {
    gap: {
      control: "select",
      options: ["none", "xs", "sm", "md", "lg", "xl"]
    },
    align: {
      control: "select",
      options: ["start", "center", "end", "stretch", "baseline"]
    },
    justify: {
      control: "select",
      options: ["start", "center", "end", "between"]
    },
    wrap: {
      control: "boolean"
    }
  }
} satisfies Meta<InlineStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Inline gap="md" align="center">
  <span>Item A</span>
  <span>Item B</span>
  <span>Item C</span>
</Inline>`
      }
    }
  }
};
