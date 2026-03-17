<script lang="ts">
  import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetHeader,
    SheetTitle,
    SheetTrigger
  } from "../ui/sheet/index.js";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import type { Snippet } from "svelte";

  export interface Props {
    open?: boolean;
    side?: "top" | "right" | "bottom" | "left";
    title?: string;
    description?: string;
    trigger?: Snippet;
    children?: Snippet;
    class?: string;
  }

  let { open = $bindable(false), side = "left", title = "Drawer", description = "Slide-over panel.", trigger, children, class: className }: Props = $props();
</script>

<Sheet bind:open>
  <SheetTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open drawer</ButtonBase>
    {/if}
  </SheetTrigger>
  <SheetContent {side} class={className}>
    <SheetHeader>
      <SheetTitle>{title}</SheetTitle>
      <SheetDescription>{description}</SheetDescription>
    </SheetHeader>
    <div class="mt-4">
      {#if children}
        {@render children()}
      {:else}
        <p class="text-sm text-muted-foreground">Drawer content goes here.</p>
      {/if}
    </div>
  </SheetContent>
</Sheet>
