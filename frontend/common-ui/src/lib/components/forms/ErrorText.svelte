<script lang="ts">
  import { cn } from "../../utils.js";
  import type { ErrorTextProps } from "./types.js";

  let { class: className, children }: ErrorTextProps = $props();
</script>

<p class={cn("text-destructive text-xs", className)} role="alert">
  {@render children?.()}
</p>
