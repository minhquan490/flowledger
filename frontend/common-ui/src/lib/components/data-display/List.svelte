<script lang="ts">
  import { cn } from "../../utils.js";

  export interface Props {
    items?: string[];
    ordered?: boolean;
    class?: string;
  }

  let { items = [], ordered = false, class: className }: Props = $props();
</script>

<svelte:element this={ordered ? "ol" : "ul"} class={cn("list-inside space-y-1", ordered ? "list-decimal" : "list-disc", className)}>
  {#each items as item, idx (`item-${idx}`)}
    <li>{item}</li>
  {/each}
</svelte:element>
