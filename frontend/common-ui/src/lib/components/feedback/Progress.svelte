<script lang="ts">
  import { Progress as UIProgress } from "../ui/progress/index.js";

  export interface Props {
    value?: number;
    max?: number;
    class?: string;
  }

  let { value = 45, max = 100, class: className }: Props = $props();
  const clamped = $derived(Math.max(0, Math.min(max, value)));
  const percent = $derived(Math.round((clamped / max) * 100));
</script>

<div class="space-y-2">
  <UIProgress value={clamped} {max} class={className} />
  <p class="text-xs text-muted-foreground">{percent}%</p>
</div>
