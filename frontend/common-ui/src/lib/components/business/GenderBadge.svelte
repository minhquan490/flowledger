<script lang="ts">
  import { Badge } from "../data-display/index.js";
  import type { BadgeVariant } from "../ui/badge/index.js";

  export interface Props {
    gender: "male" | "female" | "other" | "unknown" | string | null | undefined;
    class?: string;
  }

  let { gender = "unknown", class: className }: Props = $props();

  let variant = $derived.by<BadgeVariant>(() => {
    const g = (gender || "unknown").toLowerCase();
    if (g === "male") return "default";
    if (g === "female") return "secondary";
    return "outline";
  });

  let displayLabel = $derived.by(() => {
    const g = gender || "unknown";
    return g.charAt(0).toUpperCase() + g.slice(1).toLowerCase();
  });
</script>

<Badge {variant} class={className}>{displayLabel}</Badge>
