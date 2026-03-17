<script lang="ts">
  import Badge from "../data-display/Badge.svelte";

  export interface Props {
    label?: string;
    status?: "neutral" | "success" | "warning" | "error" | "info";
    class?: string;
  }

  let { label = "Neutral", status = "neutral", class: className }: Props = $props();

  const variant = $derived.by(() => {
    if (status === "error") return "destructive" as const;
    return "secondary" as const;
  });

  const toneClass = $derived.by(() => {
    if (status === "success") return "bg-emerald-100 text-emerald-800 hover:bg-emerald-100";
    if (status === "warning") return "bg-amber-100 text-amber-800 hover:bg-amber-100";
    if (status === "info") return "bg-sky-100 text-sky-800 hover:bg-sky-100";
    return className;
  });
</script>

<Badge {variant} class={toneClass}>{label}</Badge>
