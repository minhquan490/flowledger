import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import type { BaseButtonStoryArgs } from "@medisphere/common-ui/components/buttons/story-types";
import { DangerButton } from "../../lib";

interface DangerButtonStoryArgs extends BaseButtonStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/DangerButton",
  component: DangerButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Destructive-action button for high-risk operations like delete or revoke."
      }
    }
  },
  render: (args) => ({
    Component: DangerButton,
    props: args
  }),
  args: {
    size: "default",
    disabled: false,
    children: textSnippet("Danger Button")
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
} satisfies Meta<DangerButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<DangerButton size="default">Danger Button</DangerButton>`
      }
    }
  }
};

export const ConfirmDelete: Story = {
  args: { children: textSnippet("Delete") },
  parameters: {
    docs: {
      source: {
        code: `<DangerButton size="default">Delete</DangerButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    children: textSnippet("Click Me"),
    onclick: clickLogger("DangerButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<DangerButton onclick={handleClick}>Click Me</DangerButton>`
      }
    }
  }
};
