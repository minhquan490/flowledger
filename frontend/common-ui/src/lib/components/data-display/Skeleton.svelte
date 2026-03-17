<script lang="ts">
  import { Skeleton as UISkeleton } from "../ui/skeleton/index.js";
  import type { ComponentProps } from "svelte";

  export interface Props extends ComponentProps<typeof UISkeleton> {}

  let props: Props = $props();
</script>

<UISkeleton {...props} />
