<script lang="ts">
  import {
    CommandDialog,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
    CommandShortcut
  } from "../ui/command/index.js";

  export interface CommandMenuItem {
    value: string;
    label: string;
    keywords?: string[];
    shortcut?: string;
    disabled?: boolean;
  }

  export interface Props {
    open?: boolean;
    title?: string;
    description?: string;
    placeholder?: string;
    emptyText?: string;
    heading?: string;
    items?: CommandMenuItem[];
    onSelect?: (item: CommandMenuItem) => void;
  }

  let {
    open = $bindable(false),
    title = "Command Menu",
    description = "Search and run a command",
    placeholder = "Type a command...",
    emptyText = "No results found",
    heading = "Commands",
    items = [],
    onSelect
  }: Props = $props();
</script>

<CommandDialog bind:open {title} {description}>
  <CommandInput {placeholder} />
  <CommandList>
    <CommandEmpty>{emptyText}</CommandEmpty>
    <CommandGroup {heading}>
      {#each items as item (item.value)}
        <CommandItem
          value={item.value}
          keywords={item.keywords}
          disabled={item.disabled}
          onSelect={() => {
            onSelect?.(item);
            open = false;
          }}
        >
          <span>{item.label}</span>
          {#if item.shortcut}
            <CommandShortcut>{item.shortcut}</CommandShortcut>
          {/if}
        </CommandItem>
      {/each}
    </CommandGroup>
  </CommandList>
</CommandDialog>
