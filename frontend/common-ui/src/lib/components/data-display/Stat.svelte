<script lang="ts">
  import { cn } from "../../utils.js";

  export interface Props {
    label: string;
    value: string | number;
    change?: string;
    changeTone?: "neutral" | "positive" | "negative";
    class?: string;
  }

  let {
    label,
    value,
    change,
    changeTone = "neutral",
    class: className
  }: Props = $props();
</script>

<div class={cn("bg-card text-card-foreground rounded-lg border p-4", className)}>
  <div class="text-muted-foreground text-xs">{label}</div>
  <div class="mt-1 text-2xl font-semibold">{value}</div>
  {#if change}
    <div
      class={cn(
        "mt-1 text-xs",
        changeTone === "positive" && "text-green-600",
        changeTone === "negative" && "text-destructive",
        changeTone === "neutral" && "text-muted-foreground"
      )}
    >
      {change}
    </div>
  {/if}
</div>
