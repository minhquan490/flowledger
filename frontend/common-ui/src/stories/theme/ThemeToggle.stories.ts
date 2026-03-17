import type { Meta, StoryObj } from "@storybook/svelte-vite";
import ThemeToggleDemo from "./ThemeToggleDemo.svelte";

const meta = {
  title: "Theme/ThemeToggle",
  component: ThemeToggleDemo,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component:
          "Icon-only mode toggle that switches between dark and light themes via `mode-watcher`."
      }
    }
  }
} satisfies Meta<typeof ThemeToggleDemo>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import ThemeToggle from "$lib/theme/ThemeToggle.svelte";
</script>

<ThemeToggle />`
      }
    }
  }
};

export const InHeader: Story = {
  render: () => ({
    Component: ThemeToggleDemo
  }),
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import ThemeToggle from "$lib/theme/ThemeToggle.svelte";
</script>

<header class="flex items-center justify-end p-4 border-b">
  <ThemeToggle />
</header>`
      }
    }
  }
};
