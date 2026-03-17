import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import type { BaseButtonStoryArgs } from "@medisphere/common-ui/components/buttons/story-types";
import { GhostButton } from "../../lib";

interface GhostButtonStoryArgs extends BaseButtonStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/GhostButton",
  component: GhostButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Low-visual-noise button for contextual or tertiary actions."
      }
    }
  },
  render: (args) => ({
    Component: GhostButton,
    props: args
  }),
  args: {
    size: "default",
    disabled: false,
    children: textSnippet("Ghost Button")
  },
  argTypes: {
    size: {
      description: "Button size token",
      control: "select",
      options: ["default", "sm", "lg", "icon", "icon-sm", "icon-lg"]
    },
    disabled: {
      description: "Disable interaction",
      control: "boolean"
    },
    href: {
      description: "Render as anchor when provided",
      control: "text"
    },
    onclick: {
      description: "Click callback",
      control: false
    }
  }
} satisfies Meta<GhostButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<GhostButton size="default">Ghost Button</GhostButton>`
      }
    }
  }
};

export const Small: Story = {
  args: { size: "sm", children: textSnippet("Ghost Small") },
  parameters: {
    docs: {
      source: {
        code: `<GhostButton size="sm">Ghost Small</GhostButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    children: textSnippet("Click Me"),
    onclick: clickLogger("GhostButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<GhostButton onclick={handleClick}>Click Me</GhostButton>`
      }
    }
  }
};
