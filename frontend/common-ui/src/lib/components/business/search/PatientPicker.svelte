<script lang="ts">
  import PatientSearchBox from "./PatientSearchBox.svelte";
  import { Popover, PopoverContent, PopoverTrigger } from "../../ui/popover/index.js";
  import type { Patient } from "../../../types/business.js";
  import { ChevronsUpDown, User } from "@lucide/svelte";
  import { cn } from "../../../utils.js";

  export interface Props {
    selectedPatient?: Patient | null;
    patients: Patient[];
    loading?: boolean;
    placeholder?: string;
    onSelect?: (patient: Patient) => void;
    onSearch?: (query: string) => void;
    class?: string;
  }

  let {
    selectedPatient,
    patients = [],
    loading = false,
    placeholder = "Select patient...",
    onSelect,
    onSearch,
    class: className,
  }: Props = $props();

  let open = $state(false);

  function handleSelect(patient: Patient) {
    onSelect?.(patient);
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
      <User class="size-4 shrink-0 opacity-50" />
      {#if selectedPatient}
        <span class="truncate">{selectedPatient.name}</span>
      {:else}
        <span class="text-muted-foreground truncate">{placeholder}</span>
      {/if}
    </div>
    <ChevronsUpDown class="ml-2 size-4 shrink-0 opacity-50" />
  </PopoverTrigger>
  <PopoverContent class="w-[300px] p-0" align="start">
    <PatientSearchBox {patients} {loading} {onSearch} onSelect={handleSelect} />
  </PopoverContent>
</Popover>
