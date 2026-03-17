import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { Stack } from "../../lib";

interface StackStoryArgs {
  gap?: "none" | "xs" | "sm" | "md" | "lg" | "xl";
  align?: "start" | "center" | "end" | "stretch";
  justify?: "start" | "center" | "end" | "between";
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/Stack",
  component: Stack,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Vertical layout primitive for stacking items with consistent spacing."
      }
    }
  },
  render: (args) => ({
    Component: Stack,
    props: args
  }),
  args: {
    gap: "md",
    align: "stretch",
    justify: "start",
    children: textSnippet("Item 1\nItem 2\nItem 3")
  },
  argTypes: {
    gap: {
      control: "select",
      options: ["none", "xs", "sm", "md", "lg", "xl"]
    },
    align: {
      control: "select",
      options: ["start", "center", "end", "stretch"]
    },
    justify: {
      control: "select",
      options: ["start", "center", "end", "between"]
    }
  }
} satisfies Meta<StackStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Stack gap="md">
  <div>Item 1</div>
  <div>Item 2</div>
  <div>Item 3</div>
</Stack>`
      }
    }
  }
};

export const Tight: Story = {
  args: {
    gap: "sm"
  },
  parameters: {
    docs: {
      source: {
        code: `<Stack gap="sm">
  <div>Item 1</div>
  <div>Item 2</div>
  <div>Item 3</div>
</Stack>`
      }
    }
  }
};
