import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { Container } from "../../lib";

interface ContainerStoryArgs {
  size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full";
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/Container",
  component: Container,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Horizontal page container with max-width presets and responsive padding."
      }
    }
  },
  render: (args) => ({
    Component: Container,
    props: args
  }),
  args: {
    size: "xl",
    children: textSnippet("Container content")
  },
  argTypes: {
    size: {
      control: "select",
      options: ["sm", "md", "lg", "xl", "2xl", "full"]
    }
  }
} satisfies Meta<ContainerStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Container size="xl">Container content</Container>`
      }
    }
  }
};

export const FullWidth: Story = {
  args: {
    size: "full",
    children: textSnippet("Full width container")
  },
  parameters: {
    docs: {
      source: {
        code: `<Container size="full">Full width container</Container>`
      }
    }
  }
};
