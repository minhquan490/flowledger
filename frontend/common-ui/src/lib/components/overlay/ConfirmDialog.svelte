<script lang="ts">
  import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
  } from "../ui/dialog/index.js";
  import type { Snippet } from "svelte";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import DangerButton from "../buttons/DangerButton.svelte";
  import SecondaryButton from "../buttons/SecondaryButton.svelte";
  import { LoaderCircle } from "@lucide/svelte";

  export interface Props {
    open?: boolean;
    title?: string;
    description?: string;
    confirmText?: string;
    cancelText?: string;
    trigger?: Snippet;
    loading?: boolean;
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
    loading = false,
    onConfirm,
    onCancel,
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
        disabled={loading}
        onclick={() => {
          onConfirm?.();
          // Check if loading became true in the same tick or a microtask
          queueMicrotask(() => {
            if (!loading) open = false;
          });
        }}
      >
        {#if loading}
          <LoaderCircle class="mr-2 h-4 w-4 animate-spin" />
        {/if}
        {confirmText}
      </DangerButton>
    </DialogFooter>
  </DialogContent>
</Dialog>
