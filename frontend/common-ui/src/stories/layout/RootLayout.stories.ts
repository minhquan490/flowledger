import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Snippet } from "svelte";
import { textSnippet } from "../story-helpers";
import {RootLayout} from "../../lib";

interface RootLayoutStoryArgs {
  children?: Snippet;
}

const meta = {
  title: "Components/Layout/RootLayout",
  component: RootLayout,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component:
          "Application root layout that mounts `ModeWatcher` and applies CSS variable theme tokens from the current mode."
      }
    }
  },
  render: (args) => ({
    Component: RootLayout,
    props: args
  }),
  args: {
    children: textSnippet(`<main class="p-6">App content goes here</main>`)
  }
} satisfies Meta<RootLayoutStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import RootLayout from "$lib/components/layout/RootLayout.svelte";
</script>

<RootLayout>
  <main class="p-6">App content goes here</main>
</RootLayout>`
      }
    }
  }
};
