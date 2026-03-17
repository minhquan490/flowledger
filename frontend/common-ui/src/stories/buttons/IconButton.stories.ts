import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { createRawSnippet, type Snippet } from "svelte";
import { clickLogger } from "../story-helpers";
import type { BaseButtonStoryArgs } from "../../lib/components/buttons/story-types";
import { IconButton } from "../../lib";

function iconSnippet(): Snippet {
  return createRawSnippet(() => ({
    render: () =>
      `<svg
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="size-4"
      >
        <path d="M5 12h14" />
        <path d="M12 5v14" />
      </svg>`
  }));
}

interface IconButtonStoryArgs extends BaseButtonStoryArgs {
  ariaLabel?: string;
  children?: Snippet;
}

const meta = {
  title: "Components/Buttons/IconButton",
  component: IconButton,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Compact icon-only button built on `ButtonBase` with icon size presets."
      }
    }
  },
  render: (args) => ({
    Component: IconButton,
    props: args
  }),
  args: {
    ariaLabel: "Add",
    size: "icon",
    disabled: false,
    children: iconSnippet()
  },
  argTypes: {
    size: {
      description: "Icon button size token",
      control: "select",
      options: ["icon", "icon-sm", "icon-lg"]
    },
    ariaLabel: {
      description: "Accessible label for the icon-only button",
      control: "text"
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
} satisfies Meta<IconButtonStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import PlusIcon from "@lucide/svelte/icons/plus";
</script>

<IconButton ariaLabel="Add">
  <PlusIcon class="size-4" />
</IconButton>`
      }
    }
  }
};

export const Small: Story = {
  args: {
    size: "icon-sm",
    ariaLabel: "Open search"
  },
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import SearchIcon from "@lucide/svelte/icons/search";
</script>

<IconButton size="icon-sm" ariaLabel="Open search">
  <SearchIcon class="size-4" />
</IconButton>`
      }
    }
  }
};

export const Large: Story = {
  args: {
    size: "icon-lg",
    ariaLabel: "Create record"
  },
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import PlusIcon from "@lucide/svelte/icons/plus";
</script>

<IconButton size="icon-lg" ariaLabel="Create record">
  <PlusIcon class="size-5" />
</IconButton>`
      }
    }
  }
};

export const OnClick: Story = {
  args: {
    ariaLabel: "Add item",
    onclick: clickLogger("IconButton")
  },
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import PlusIcon from "@lucide/svelte/icons/plus";

  function handleClick() {
    console.log("Icon button clicked");
  }
</script>

<IconButton ariaLabel="Add item" onclick={handleClick}>
  <PlusIcon class="size-4" />
</IconButton>`
      }
    }
  }
};
