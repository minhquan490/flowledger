<script lang="ts">
  export interface Props {
    status?: "neutral" | "success" | "warning" | "error" | "info";
    pulse?: boolean;
    showLabel?: boolean;
    label?: string;
    class?: string;
  }

  let {
    status = "neutral",
    pulse = false,
    showLabel = true,
    label,
    class: className
  }: Props = $props();

  const colorClass = $derived.by(() => {
    if (status === "success") return "bg-emerald-500";
    if (status === "warning") return "bg-amber-500";
    if (status === "error") return "bg-red-500";
    if (status === "info") return "bg-sky-500";
    return "bg-slate-400";
  });

  const resolvedLabel = $derived.by(() => {
    if (label) return label;
    if (status === "success") return "Success";
    if (status === "warning") return "Warning";
    if (status === "error") return "Error";
    if (status === "info") return "Info";
    return "Neutral";
  });
</script>

<span class={`inline-flex items-center gap-2 ${className ?? ""}`.trim()}>
  <span class={`inline-block h-2.5 w-2.5 rounded-full ${colorClass} ${pulse ? "animate-pulse" : ""}`.trim()} aria-label={`status-${status}`}></span>
  {#if showLabel}
    <span class="text-sm text-muted-foreground">{resolvedLabel}</span>
  {/if}
</span>
