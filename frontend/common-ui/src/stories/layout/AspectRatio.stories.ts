import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { AspectRatio } from "../../lib";

interface AspectRatioStoryArgs {
  ratio?: number;
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/AspectRatio",
  component: AspectRatio,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Aspect-ratio wrapper composed from shadcn AspectRatio primitive."
      }
    }
  },
  render: (args) => ({
    Component: AspectRatio,
    props: args
  }),
  args: {
    ratio: 16 / 9,
    children: textSnippet("Media placeholder")
  }
} satisfies Meta<AspectRatioStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Widescreen: Story = {
  parameters: {
    docs: {
      source: {
        code: `<AspectRatio ratio={16 / 9}>
  <div class="bg-muted flex h-full items-center justify-center rounded-md">
    Widescreen media
  </div>
</AspectRatio>`
      }
    }
  }
};

export const Square: Story = {
  args: {
    ratio: 1
  },
  parameters: {
    docs: {
      source: {
        code: `<AspectRatio ratio={1}>
  <div class="bg-muted flex h-full items-center justify-center rounded-md">
    Square media
  </div>
</AspectRatio>`
      }
    }
  }
};
