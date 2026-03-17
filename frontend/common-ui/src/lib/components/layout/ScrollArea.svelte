<script lang="ts">
  import { ScrollArea as UIScrollArea } from "../ui/scroll-area/index.js";
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    viewportClass?: string;
    orientation?: "vertical" | "horizontal" | "both";
    children?: Snippet;
  }

  let {
    class: className,
    viewportClass,
    orientation = "vertical",
    children
  }: Props = $props();
</script>

<UIScrollArea class={cn("h-72 w-full", className)} {orientation}>
  <div class={cn("p-4", viewportClass)}>
    {@render children?.()}
  </div>
</UIScrollArea>
