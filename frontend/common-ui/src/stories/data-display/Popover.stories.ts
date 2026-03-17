import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Popover } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Popover",
  component: Popover,
  tags: ["autodocs"],
  args: {
    trigger: textSnippet("Open"),
    content: textSnippet("Popover content")
  }
} satisfies Meta<typeof Popover>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">
  import { createRawSnippet } from "svelte";

  const trigger = createRawSnippet(() => ({
    render: () => "Open"
  }));

  const content = createRawSnippet(() => ({
    render: () => "Popover content"
  }));
</script>

<Popover {trigger} {content} />`
      }
    }
  }
};
