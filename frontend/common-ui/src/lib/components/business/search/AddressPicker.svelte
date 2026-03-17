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
  import type { Address } from "../../../types/business.js";
  import { ChevronsUpDown, MapPin } from "@lucide/svelte";

  export interface Props {
    selectedAddress?: Address | null;
    addresses: Address[];
    loading?: boolean;
    placeholder?: string;
    onSelect?: (address: Address) => void;
    class?: string;
  }

  let {
    selectedAddress,
    addresses = [],
    loading = false,
    placeholder = "Select address...",
    onSelect,
    class: className,
  }: Props = $props();

  let open = $state(false);
  let query = $state("");

  function handleSelect(address: Address) {
    onSelect?.(address);
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
      <MapPin class="size-4 shrink-0 opacity-50" />
      {#if selectedAddress}
        <span class="truncate"
          >{selectedAddress.line1}, {selectedAddress.city}</span
        >
      {:else}
        <span class="text-muted-foreground truncate">{placeholder}</span>
      {/if}
    </div>
    <ChevronsUpDown class="ml-2 size-4 shrink-0 opacity-50" />
  </PopoverTrigger>
  <PopoverContent class="w-75 p-0" align="start">
    <Command>
      <CommandInput placeholder="Search address..." bind:value={query} />
      <CommandList>
        {#if loading}
          <CommandLoading class="px-3 py-2 text-sm">Searching...</CommandLoading>
        {:else if addresses.length === 0}
          <CommandEmpty>No addresses found.</CommandEmpty>
        {:else}
          <CommandGroup>
            {#each addresses as address (address.id)}
              <CommandItem
                value={`${address.line1} ${address.city}`}
                onSelect={() => handleSelect(address)}
                class="cursor-pointer"
              >
                <div class="flex flex-col">
                  <span class="font-medium text-sm">{address.line1}</span>
                  <span class="text-muted-foreground text-xs">
                    {address.city}, {address.state}
                    {address.postalCode}
                  </span>
                </div>
              </CommandItem>
            {/each}
          </CommandGroup>
        {/if}
      </CommandList>
    </Command>
  </PopoverContent>
</Popover>
