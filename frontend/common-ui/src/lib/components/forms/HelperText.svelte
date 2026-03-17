<script lang="ts">
  import { cn } from "../../utils.js";
  import type { HelperTextProps } from "./types.js";

  let { class: className, children }: HelperTextProps = $props();
</script>

<p class={cn("text-muted-foreground text-xs", className)}>
  {@render children?.()}
</p>
