<script lang="ts">
  import { CardTitle as UICardTitle } from "../ui/card/index.js";
  import type { HTMLAttributes } from "svelte/elements";
  import type { Snippet } from "svelte";

  export interface Props extends HTMLAttributes<HTMLHeadingElement> {
    children?: Snippet;
  }

  let { children, class: className, ...restProps }: Props = $props();
</script>

<UICardTitle class={className} {...restProps}>
  {@render children?.()}
</UICardTitle>
