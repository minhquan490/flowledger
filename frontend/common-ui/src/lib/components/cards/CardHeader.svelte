<script lang="ts">
  import { CardHeader as UICardHeader } from "../ui/card/index.js";
  import type { HTMLAttributes } from "svelte/elements";
  import type { Snippet } from "svelte";

  export interface Props extends HTMLAttributes<HTMLDivElement> {
    children?: Snippet;
  }

  let { children, class: className, ...restProps }: Props = $props();
</script>

<UICardHeader class={className} {...restProps}>
  {@render children?.()}
</UICardHeader>
