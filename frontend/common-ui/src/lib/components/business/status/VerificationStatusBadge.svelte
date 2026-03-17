<script lang="ts">
  import { Badge } from "../../data-display/index.js";
  import type { BadgeVariant } from "../../ui/badge/index.js";

  export interface Props {
    status:
      | "unverified"
      | "pending"
      | "verified"
      | "rejected"
      | string
      | null
      | undefined;
    class?: string;
  }

  let { status = "unverified", class: className }: Props = $props();

  let variant = $derived.by<BadgeVariant>(() => {
    const s = (status || "unverified").toLowerCase();
    switch (s) {
      case "unverified":
        return "outline";
      case "pending":
        return "secondary";
      case "verified":
        return "default";
      case "rejected":
        return "destructive";
      default:
        return "outline";
    }
  });

  let displayLabel = $derived.by(() => {
    const s = status || "unverified";
    return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
  });
</script>

<Badge {variant} class={className}>{displayLabel}</Badge>
