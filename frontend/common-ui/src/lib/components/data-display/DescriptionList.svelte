<script lang="ts">
  import { cn } from "../../utils.js";

  export interface DescriptionItem {
    term: string;
    description: string;
  }

  export interface Props {
    items?: DescriptionItem[];
    class?: string;
  }

  let { items = [], class: className }: Props = $props();
</script>

<dl class={cn("grid gap-3", className)}>
  {#each items as item, idx (`desc-${idx}`)}
    <div class="grid gap-1">
      <dt class="text-muted-foreground text-sm">{item.term}</dt>
      <dd class="text-sm font-medium">{item.description}</dd>
    </div>
  {/each}
</dl>
