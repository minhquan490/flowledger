<script lang="ts">
  import {
    Sheet as UISheet,
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

  let { open = $bindable(false), side = "right", title = "Sheet", description = "Panel content.", trigger, children, class: className }: Props = $props();
</script>

<UISheet bind:open>
  <SheetTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open sheet</ButtonBase>
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
        <p class="text-sm text-muted-foreground">Sheet content goes here.</p>
      {/if}
    </div>
  </SheetContent>
</UISheet>
