<script lang="ts">
  import { Separator } from "../ui/separator/index.js";

  export interface Props {
    class?: string;
    orientation?: "horizontal" | "vertical";
    decorative?: boolean;
  }

  let {
    class: className,
    orientation = "horizontal",
    decorative = true
  }: Props = $props();
</script>

<Separator {orientation} {decorative} class={className} />
