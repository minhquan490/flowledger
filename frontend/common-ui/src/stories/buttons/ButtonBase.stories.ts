import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { clickLogger, textSnippet } from "../story-helpers";
import { ButtonBase } from "../../lib";

interface ButtonBaseStoryArgs extends BaseButtonStoryArgs {
  variant?: ButtonBaseVariant;
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/ButtonBase",
  component: ButtonBase,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component:
          "Reusable base wrapper for app buttons. It maps business variants (`primary`, `outline`, `secondary`, `ghost`, `danger`) to the shadcn button primitive."
      }
    }
  },
  render: (args) => ({
    Component: ButtonBase,
    props: args
  }),
  args: {
    variant: "primary",
    size: "default",
    disabled: false,
    children: textSnippet("Button")
  },
  argTypes: {
    variant: {
      description: "Visual variant mapped to shadcn button variants",
      control: "select",
      options: ["primary", "outline", "secondary", "ghost", "danger"]
    },
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
} satisfies Meta<ButtonBaseStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: { variant: "primary", children: textSnippet("Primary") },
  parameters: {
    docs: {
      source: {
        code: `< ButtonBase variant = "primary" > Primary </ButtonBase>`
      }
    }
  }
};

export const Outline: Story = {
  args: { variant: "outline", children: textSnippet("Outline") },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="outline">Outline</ButtonBase>`
      }
    }
  }
};

export const Secondary: Story = {
  args: { variant: "secondary", children: textSnippet("Secondary") },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="secondary">Secondary</ButtonBase>`
      }
    }
  }
};

export const Ghost: Story = {
  args: { variant: "ghost", children: textSnippet("Ghost") },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="ghost">Ghost</ButtonBase>`
      }
    }
  }
};

export const Danger: Story = {
  args: { variant: "danger", children: textSnippet("Danger") },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="danger">Danger</ButtonBase>`
      }
    }
  }
};

export const Disabled: Story = {
  args: { variant: "primary", disabled: true, children: textSnippet("Disabled") },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="primary" disabled={true}>Disabled</ButtonBase>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    variant: "primary",
    children: textSnippet("Click Me"),
    onclick: clickLogger("ButtonBase")
  },
  parameters: {
    docs: {
      source: {
        code: `<ButtonBase variant="primary" onclick={handleClick}>Click Me</ButtonBase>`
      }
    }
  }
};
