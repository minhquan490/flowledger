<script lang="ts">
  import {
    Popover as UIPopover,
    PopoverContent,
    PopoverTrigger
  } from "../ui/popover/index.js";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import type { Snippet } from "svelte";

  export interface Props {
    open?: boolean;
    trigger?: Snippet;
    content?: Snippet;
    class?: string;
  }

  let {
    open = $bindable(false),
    trigger,
    content,
    class: className
  }: Props = $props();
</script>

<UIPopover bind:open>
  <PopoverTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open</ButtonBase>
    {/if}
  </PopoverTrigger>
  <PopoverContent class={className}>
    {#if content}
      {@render content()}
    {:else}
      <p class="text-sm">Popover content</p>
    {/if}
  </PopoverContent>
</UIPopover>
