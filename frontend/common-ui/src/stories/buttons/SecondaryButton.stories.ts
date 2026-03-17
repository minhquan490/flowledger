import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import type { BaseButtonStoryArgs } from "@medisphere/common-ui/components/buttons/story-types";
import { SecondaryButton } from "../../lib";

interface SecondaryButtonStoryArgs extends BaseButtonStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/SecondaryButton",
  component: SecondaryButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Neutral button for supporting actions when primary emphasis is not required."
      }
    }
  },
  render: (args) => ({
    Component: SecondaryButton,
    props: args
  }),
  args: {
    size: "default",
    disabled: false,
    children: textSnippet("Secondary Button")
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
} satisfies Meta<SecondaryButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<SecondaryButton size="default">Secondary Button</SecondaryButton>`
      }
    }
  }
};

export const Icon: Story = {
  args: { size: "icon", children: textSnippet("+") },
  parameters: {
    docs: {
      source: {
        code: `<SecondaryButton size="icon">+</SecondaryButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    children: textSnippet("Click Me"),
    onclick: clickLogger("SecondaryButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<SecondaryButton onclick={handleClick}>Click Me</SecondaryButton>`
      }
    }
  }
};
