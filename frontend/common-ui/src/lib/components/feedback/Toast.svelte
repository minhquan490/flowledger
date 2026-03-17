<script lang="ts">
  import { Toaster } from "../ui/sonner/index.js";
  import { toast } from "svelte-sonner";
  import ButtonBase from "../buttons/ButtonBase.svelte";

  export interface Props {
    position?: "top-left" | "top-center" | "top-right" | "bottom-left" | "bottom-center" | "bottom-right";
    richColors?: boolean;
    expand?: boolean;
    closeButton?: boolean;
    showDemoButton?: boolean;
    demoLabel?: string;
    demoMessage?: string;
    class?: string;
  }

  let {
    position = "top-right",
    richColors = true,
    expand = false,
    closeButton = true,
    showDemoButton = true,
    demoLabel = "Show toast",
    demoMessage = "Operation completed.",
    class: className
  }: Props = $props();
</script>

<Toaster {position} {richColors} {expand} {closeButton} />

{#if showDemoButton}
  <ButtonBase variant="outline" class={className} onclick={() => toast.success(demoMessage)}>{demoLabel}</ButtonBase>
{/if}
