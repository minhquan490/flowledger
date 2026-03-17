<script lang="ts">
  import { Badge as UIBadge, type BadgeVariant } from "../ui/badge/index.js";
  import type { Snippet } from "svelte";

  export interface Props {
    variant?: BadgeVariant;
    class?: string;
    href?: string;
    children?: Snippet;
  }

  let { variant = "default", class: className, href, children }: Props = $props();
</script>

<UIBadge {variant} class={className} {href}>
  {@render children?.()}
</UIBadge>
