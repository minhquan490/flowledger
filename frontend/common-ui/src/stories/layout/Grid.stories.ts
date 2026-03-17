import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { Grid } from "../../lib";

interface GridStoryArgs {
  cols?: 1 | 2 | 3 | 4 | 6 | 12;
  gap?: "none" | "xs" | "sm" | "md" | "lg" | "xl";
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/Grid",
  component: Grid,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Grid layout primitive with configurable column count and spacing."
      }
    }
  },
  render: (args) => ({
    Component: Grid,
    props: args
  }),
  args: {
    cols: 3,
    gap: "md",
    children: textSnippet("Cell 1\nCell 2\nCell 3")
  },
  argTypes: {
    cols: {
      control: "select",
      options: [1, 2, 3, 4, 6, 12]
    },
    gap: {
      control: "select",
      options: ["none", "xs", "sm", "md", "lg", "xl"]
    }
  }
} satisfies Meta<GridStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Grid cols={3} gap="md">
  <div>Cell 1</div>
  <div>Cell 2</div>
  <div>Cell 3</div>
</Grid>`
      }
    }
  }
};

export const TwoColumn: Story = {
  args: {
    cols: 2
  },
  parameters: {
    docs: {
      source: {
        code: `<Grid cols={2} gap="md">
  <div>Cell 1</div>
  <div>Cell 2</div>
</Grid>`
      }
    }
  }
};
