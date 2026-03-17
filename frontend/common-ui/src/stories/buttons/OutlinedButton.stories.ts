import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import type { BaseButtonStoryArgs } from "@medisphere/common-ui/components/buttons/story-types";
import { OutlinedButton } from "../../lib";

interface OutlinedButtonStoryArgs extends BaseButtonStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/OutlinedButton",
  component: OutlinedButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Secondary emphasis button with outline style, for less prominent actions."
      }
    }
  },
  render: (args) => ({
    Component: OutlinedButton,
    props: args
  }),
  args: {
    size: "default",
    disabled: false,
    children: textSnippet("Outlined Button")
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
} satisfies Meta<OutlinedButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<OutlinedButton size="default">Outlined Button</OutlinedButton>`
      }
    }
  }
};

export const Large: Story = {
  args: { size: "lg", children: textSnippet("Large") },
  parameters: {
    docs: {
      source: {
        code: `<OutlinedButton size="lg">Large</OutlinedButton>`
      }
    }
  }
};

export const Disabled: Story = {
  args: { disabled: true, children: textSnippet("Disabled") },
  parameters: {
    docs: {
      source: {
        code: `<OutlinedButton disabled={true}>Disabled</OutlinedButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    children: textSnippet("Click Me"),
    onclick: clickLogger("OutlinedButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<OutlinedButton onclick={handleClick}>Click Me</OutlinedButton>`
      }
    }
  }
};
