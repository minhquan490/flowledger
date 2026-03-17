<script lang="ts">
  import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
  } from "../ui/dialog/index.js";
  import type { Snippet } from "svelte";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import DangerButton from "../buttons/DangerButton.svelte";
  import SecondaryButton from "../buttons/SecondaryButton.svelte";

  export interface Props {
    open?: boolean;
    title?: string;
    description?: string;
    confirmText?: string;
    cancelText?: string;
    trigger?: Snippet;
    onConfirm?: () => void;
    onCancel?: () => void;
  }

  let {
    open = $bindable(false),
    title = "Confirm action",
    description = "Are you sure you want to continue?",
    confirmText = "Confirm",
    cancelText = "Cancel",
    trigger,
    onConfirm,
    onCancel
  }: Props = $props();
</script>

<Dialog bind:open>
  <DialogTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open confirm</ButtonBase>
    {/if}
  </DialogTrigger>
  <DialogContent>
    <DialogHeader>
      <DialogTitle>{title}</DialogTitle>
      <DialogDescription>{description}</DialogDescription>
    </DialogHeader>
    <DialogFooter>
      <SecondaryButton
        onclick={() => {
          onCancel?.();
          open = false;
        }}
      >
        {cancelText}
      </SecondaryButton>
      <DangerButton
        onclick={() => {
          onConfirm?.();
          open = false;
        }}
      >
        {confirmText}
      </DangerButton>
    </DialogFooter>
  </DialogContent>
</Dialog>
