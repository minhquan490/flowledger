<script lang="ts">
  import { CardContent as UICardContent } from "../ui/card/index.js";
  import type { HTMLAttributes } from "svelte/elements";
  import type { Snippet } from "svelte";

  export interface Props extends HTMLAttributes<HTMLDivElement> {
    children?: Snippet;
  }

  let { children, class: className, ...restProps }: Props = $props();
</script>

<UICardContent class={className} {...restProps}>
  {@render children?.()}
</UICardContent>
