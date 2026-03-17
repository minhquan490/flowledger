<script lang="ts">
  import { Card as UICard, CardContent } from "../ui/card/index.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    contentClass?: string;
    children?: Snippet;
  }

  let { class: className, contentClass, children }: Props = $props();
</script>

<UICard class={className}>
  <CardContent class={contentClass}>
    {@render children?.()}
  </CardContent>
</UICard>
