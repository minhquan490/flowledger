<script lang="ts">
  import type { RequiredMarkProps } from "./types.js";

  let { class: className }: RequiredMarkProps = $props();
</script>

<span class={className} aria-hidden="true">*</span>
