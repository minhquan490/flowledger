<script lang="ts">
  import {
    Tooltip as UITooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger
  } from "../ui/tooltip/index.js";
  import type { Snippet } from "svelte";

  export interface Props {
    content: string;
    side?: "top" | "right" | "bottom" | "left";
    class?: string;
    children?: Snippet;
  }

  let { content, side = "top", class: className, children }: Props = $props();
</script>

<TooltipProvider>
  <UITooltip>
    <TooltipTrigger>
      {#snippet child({ props }: { props: Record<string, unknown> })}
        <span {...props} class={className}>
          {@render children?.()}
        </span>
      {/snippet}
    </TooltipTrigger>
    <TooltipContent {side}>{content}</TooltipContent>
  </UITooltip>
</TooltipProvider>
