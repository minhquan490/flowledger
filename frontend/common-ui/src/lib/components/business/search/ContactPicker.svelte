<script lang="ts">
  import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
    CommandLoading,
  } from "../../ui/command/index.js";
  import {
    Popover,
    PopoverContent,
    PopoverTrigger,
  } from "../../ui/popover/index.js";
  import { cn } from "../../../utils.js";
  import type { Contact } from "../../../types/business.js";
  import { ChevronsUpDown, Contact as ContactIcon } from "@lucide/svelte";

  export interface Props {
    selectedContact?: Contact | null;
    contacts: Contact[];
    loading?: boolean;
    placeholder?: string;
    onSelect?: (contact: Contact) => void;
    class?: string;
  }

  let {
    selectedContact,
    contacts = [],
    loading = false,
    placeholder = "Select contact...",
    onSelect,
    class: className,
  }: Props = $props();

  let open = $state(false);
  let query = $state("");

  function handleSelect(contact: Contact) {
    onSelect?.(contact);
    open = false;
  }
</script>

<Popover bind:open>
  <PopoverTrigger
    class={cn(
      "bg-background hover:bg-accent hover:text-accent-foreground dark:bg-input/30 dark:border-input dark:hover:bg-input/50 inline-flex h-9 w-full items-center justify-between rounded-md border px-4 py-2 text-sm font-medium whitespace-nowrap shadow-xs transition-all outline-none focus-visible:ring-[3px] disabled:pointer-events-none disabled:opacity-50 aria-expanded:bg-accent",
      className,
    )}
    role="combobox"
    aria-expanded={open}
  >
    <div class="flex items-center gap-2 truncate">
      <ContactIcon class="size-4 shrink-0 opacity-50" />
      {#if selectedContact}
        <span class="truncate">{selectedContact.name}</span>
      {:else}
        <span class="text-muted-foreground truncate">{placeholder}</span>
      {/if}
    </div>
    <ChevronsUpDown class="ml-2 size-4 shrink-0 opacity-50" />
  </PopoverTrigger>
  <PopoverContent class="w-75 p-0" align="start">
    <Command>
      <CommandInput placeholder="Search contact..." bind:value={query} />
      <CommandList>
        {#if loading}
          <CommandLoading class="px-3 py-2 text-sm">Searching...</CommandLoading>
        {:else if contacts.length === 0}
          <CommandEmpty>No contacts found.</CommandEmpty>
        {:else}
          <CommandGroup>
            {#each contacts as contact (contact.id)}
              <CommandItem
                value={contact.name}
                onSelect={() => handleSelect(contact)}
                class="cursor-pointer"
              >
                <div class="flex flex-col">
                  <span class="font-medium">{contact.name}</span>
                  {#if contact.relationship}
                    <span class="text-muted-foreground text-xs"
                      >{contact.relationship}</span
                    >
                  {/if}
                </div>
              </CommandItem>
            {/each}
          </CommandGroup>
        {/if}
      </CommandList>
    </Command>
  </PopoverContent>
</Popover>
