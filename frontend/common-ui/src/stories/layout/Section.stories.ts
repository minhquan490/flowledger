import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import { Section } from "../../lib";

interface SectionStoryArgs {
  as?: "section" | "div" | "article";
  spacing?: "none" | "sm" | "md" | "lg" | "xl";
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/Section",
  component: Section,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Semantic block wrapper with consistent vertical spacing presets."
      }
    }
  },
  render: (args) => ({
    Component: Section,
    props: args
  }),
  args: {
    as: "section",
    spacing: "md",
    children: textSnippet("Section content")
  },
  argTypes: {
    as: {
      control: "select",
      options: ["section", "div", "article"]
    },
    spacing: {
      control: "select",
      options: ["none", "sm", "md", "lg", "xl"]
    }
  }
} satisfies Meta<SectionStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Section spacing="md">Section content</Section>`
      }
    }
  }
};

export const Loose: Story = {
  args: {
    spacing: "xl"
  },
  parameters: {
    docs: {
      source: {
        code: `<Section spacing="xl">Section content</Section>`
      }
    }
  }
};
