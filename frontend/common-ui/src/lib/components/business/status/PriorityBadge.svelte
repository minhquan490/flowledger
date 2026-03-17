<script lang="ts">
  import { Badge } from "../../data-display/index.js";
  import type { BadgeVariant } from "../../ui/badge/index.js";

  export interface Props {
    priority: "low" | "medium" | "high" | "urgent" | string | null | undefined;
    class?: string;
  }

  let { priority = "medium", class: className }: Props = $props();

  let variant = $derived.by<BadgeVariant>(() => {
    const p = (priority || "medium").toLowerCase();
    switch (p) {
      case "low":
        return "secondary";
      case "medium":
        return "default";
      case "high":
        return "default"; // Might need a 'warning' variant if it exists, but sticking to available ones
      case "urgent":
        return "destructive";
      default:
        return "outline";
    }
  });

  let displayLabel = $derived.by(() => {
    const p = priority || "medium";
    return p.charAt(0).toUpperCase() + p.slice(1).toLowerCase();
  });
</script>

<Badge {variant} class={className}>{displayLabel}</Badge>
