<script lang="ts">
  import {
    DropdownMenu as UIDropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
  } from "../ui/dropdown-menu/index.js";
  import ButtonBase from "../buttons/ButtonBase.svelte";
  import type { Snippet } from "svelte";

  export interface MenuItem {
    label: string;
    disabled?: boolean;
    onSelect?: () => void;
  }

  export interface Props {
    items?: MenuItem[];
    trigger?: Snippet;
  }

  let { items = [], trigger }: Props = $props();
</script>

<UIDropdownMenu>
  <DropdownMenuTrigger>
    {#if trigger}
      {@render trigger()}
    {:else}
      <ButtonBase variant="outline">Open menu</ButtonBase>
    {/if}
  </DropdownMenuTrigger>
  <DropdownMenuContent>
    {#each items as item (item.label)}
      <DropdownMenuItem disabled={item.disabled} onSelect={item.onSelect}>{item.label}</DropdownMenuItem>
    {/each}
  </DropdownMenuContent>
</UIDropdownMenu>
