import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import type { BaseButtonStoryArgs } from "@medisphere/common-ui/components/buttons/story-types";
import { PrimaryButton } from "../../lib";

interface PrimaryButtonStoryArgs extends BaseButtonStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/PrimaryButton",
  component: PrimaryButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Primary call-to-action button composed from `ButtonBase` and the shadcn button primitive."
      }
    }
  },
  render: (args) => ({
    Component: PrimaryButton,
    props: args
  }),
  args: {
    size: "default",
    disabled: false,
    children: textSnippet("Primary Button")
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
} satisfies Meta<PrimaryButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<PrimaryButton size="default">Primary Button</PrimaryButton>`
      }
    }
  }
};

export const Small: Story = {
  args: { size: "sm", children: textSnippet("Small") },
  parameters: {
    docs: {
      source: {
        code: `<PrimaryButton size="sm">Small</PrimaryButton>`
      }
    }
  }
};

export const Disabled: Story = {
  args: { disabled: true, children: textSnippet("Disabled") },
  parameters: {
    docs: {
      source: {
        code: `<PrimaryButton disabled={true}>Disabled</PrimaryButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    children: textSnippet("Click Me"),
    onclick: clickLogger("PrimaryButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<PrimaryButton onclick={handleClick}>Click Me</PrimaryButton>`
      }
    }
  }
};
