<script lang="ts">
  import { AspectRatio as UIAspectRatio } from "../ui/aspect-ratio/index.js";
  import type { Snippet } from "svelte";

  export interface Props {
    ratio?: number;
    class?: string;
    children?: Snippet;
  }

  let { ratio = 16 / 9, class: className, children }: Props = $props();
</script>

<UIAspectRatio {ratio} class={className}>
  {@render children?.()}
</UIAspectRatio>
