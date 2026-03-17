<script lang="ts">
  import { Badge } from "../../data-display/index.js";
  import type { BadgeVariant } from "../../ui/badge/index.js";

  export interface Props {
    status:
      | "scheduled"
      | "checked-in"
      | "in-consultation"
      | "completed"
      | "cancelled"
      | "no-show"
      | string
      | null
      | undefined;
    class?: string;
  }

  let { status = "scheduled", class: className }: Props = $props();

  let variant = $derived.by<BadgeVariant>(() => {
    const s = (status || "scheduled").toLowerCase();
    switch (s) {
      case "scheduled":
        return "outline";
      case "checked-in":
        return "secondary";
      case "in-consultation":
        return "default";
      case "completed":
        return "default";
      case "cancelled":
        return "destructive";
      case "no-show":
        return "destructive";
      default:
        return "outline";
    }
  });

  let displayLabel = $derived.by(() => {
    const s = (status || "scheduled").replace("-", " ");
    return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
  });
</script>

<Badge {variant} class={className}>{displayLabel}</Badge>
