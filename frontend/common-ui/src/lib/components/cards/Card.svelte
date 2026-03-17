<script lang="ts">
  import { Card as UICard } from "../ui/card/index.js";
  import type { HTMLAttributes } from "svelte/elements";
  import type { Snippet } from "svelte";

  export interface CardProps extends HTMLAttributes<HTMLDivElement> {
    children?: Snippet;
  }

  let { children, class: className, ...restProps }: CardProps = $props();
</script>

<UICard class={className} {...restProps}>
  {@render children?.()}
</UICard>
