<script lang="ts">
  import {
    Dialog as UIDialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
  } from "../ui/dialog/index.js";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import type { Snippet } from "svelte";

  export interface Props {
    open?: boolean;
    title?: string;
    description?: string;
    trigger?: Snippet;
    class?: string;
    children?: Snippet;
  }

  let { open = $bindable(false), title = "Dialog", description = "Dialog content.", trigger, class: className, children }: Props = $props();
</script>

<UIDialog bind:open>
  <DialogTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open dialog</ButtonBase>
    {/if}
  </DialogTrigger>
  <DialogContent class={className}>
    <DialogHeader>
      <DialogTitle>{title}</DialogTitle>
      <DialogDescription>{description}</DialogDescription>
    </DialogHeader>
    {#if children}
      {@render children()}
    {:else}
      <p class="text-sm text-muted-foreground">Use this area for modal content.</p>
    {/if}
    <DialogFooter />
  </DialogContent>
</UIDialog>
