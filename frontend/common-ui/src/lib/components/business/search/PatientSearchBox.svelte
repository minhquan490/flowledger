<script lang="ts">
  import {
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
    CommandLoading,
    Command as CommandRoot,
  } from "../../ui/command/index.js";
  import type { Patient } from "../../../types/business.js";

  export interface Props {
    patients: Patient[];
    loading?: boolean;
    placeholder?: string;
    onSelect?: (patient: Patient) => void;
    onSearch?: (query: string) => void;
  }

  let {
    patients = [],
    loading = false,
    placeholder = "Search patient...",
    onSelect,
    onSearch,
  }: Props = $props();

  let query = $state("");

  $effect(() => {
    onSearch?.(query);
  });
</script>

<CommandRoot class="rounded-lg border shadow-md">
  <CommandInput bind:value={query} {placeholder} />
  <CommandList>
    {#if loading}
      <CommandLoading class="px-3 py-2 text-sm">Searching...</CommandLoading>
    {:else if patients.length === 0}
      <CommandEmpty>No patients found.</CommandEmpty>
    {:else}
      <CommandGroup heading="Results">
        {#each patients as patient (patient.id)}
          <CommandItem
            value={patient.name}
            onSelect={() => onSelect?.(patient)}
            class="cursor-pointer"
          >
            <div class="flex flex-col">
              <span class="font-medium">{patient.name}</span>
              {#if patient.identifier}
                <span class="text-muted-foreground text-xs"
                  >{patient.identifier}</span
                >
              {/if}
            </div>
          </CommandItem>
        {/each}
      </CommandGroup>
    {/if}
  </CommandList>
</CommandRoot>
