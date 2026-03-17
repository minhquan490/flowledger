<script lang="ts">
  import Badge from "./Badge.svelte";
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    children?: Snippet;
  }

  let { class: className, children }: Props = $props();
</script>

<Badge variant="outline" class={cn("rounded-full px-3 py-1", className)}>
  {@render children?.()}
</Badge>
