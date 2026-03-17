<script lang="ts">
  export interface Props {
    size?: "sm" | "md" | "lg";
    class?: string;
  }

  let { size = "md", class: className }: Props = $props();

  const sizeClass = $derived.by(() => {
    if (size === "sm") return "h-4 w-4 border-2";
    if (size === "lg") return "h-8 w-8 border-[3px]";
    return "h-6 w-6 border-2";
  });
</script>

<div class={`inline-block animate-spin rounded-full border-solid border-current border-r-transparent ${sizeClass} ${className ?? ""}`.trim()} aria-label="Loading"></div>
