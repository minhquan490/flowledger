<script lang="ts">
  import { Badge } from "../../data-display/index.js";
  import type { BadgeVariant } from "../../ui/badge/index.js";

  export interface Props {
    status:
      | "active"
      | "inactive"
      | "deceased"
      | "archived"
      | "unknown"
      | string
      | null
      | undefined;
    class?: string;
  }

  let { status = "unknown", class: className }: Props = $props();

  let variant = $derived.by<BadgeVariant>(() => {
    const s = (status || "unknown").toLowerCase();
    switch (s) {
      case "active":
        return "default"; // Green if primary is green, but default is usually primary
      case "inactive":
        return "secondary";
      case "deceased":
        return "destructive";
      case "archived":
        return "outline";
      default:
        return "outline";
    }
  });

  let displayLabel = $derived.by(() => {
    const s = status || "unknown";
    return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
  });
</script>

<Badge {variant} class={className}>{displayLabel}</Badge>
