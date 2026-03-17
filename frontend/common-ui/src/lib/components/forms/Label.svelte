<script lang="ts">
  import { Label as UILabel } from "../ui/label/index.js";
  import RequiredMark from "./RequiredMark.svelte";
  import type { LabelProps } from "./types.js";

  let { required = false, children, ...restProps }: LabelProps = $props();
</script>

<UILabel {...restProps}>
  {@render children?.()}
  {#if required}
    <RequiredMark class="text-destructive ms-1" />
  {/if}
</UILabel>
