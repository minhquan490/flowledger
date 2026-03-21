<script lang="ts" generics="T extends Record<string, unknown>">
  import {
    ShadcnInput,
    Pagination,
    ExpandableTable,
    type DataTableColumn
  } from '@medisphere/common-ui';
  import { Search } from '@lucide/svelte';
  import type { Snippet } from 'svelte';

  interface Props {
    data?: T[];
    columns?: DataTableColumn[];
    searchable?: boolean;
    searchKeys?: string[]; // Array of keys to search
    searchPlaceholder?: string;
    perPage?: number;
    rowKey?: string;
    emptyText?: string;
    
    // Snippets for ExpandableTable
    cell?: Snippet<[{ item: T; column: DataTableColumn; value: unknown }]>;
    expanded?: Snippet<[{ item: T }]>;
    header?: Snippet<[{ column: DataTableColumn }]>;
    headerAction?: Snippet;
  }

  let {
    data = [],
    columns = [],
    searchable = true,
    searchKeys = [],
    searchPlaceholder = "Search...",
    perPage = 10,
    rowKey = "id",
    emptyText = "No records found",
    cell,
    expanded,
    header,
    headerAction
  }: Props = $props();

  let searchQuery = $state("");
  let currentPage = $state(1);

  let filteredData = $derived.by(() => {
    if (!searchQuery) return data;
    if (searchKeys.length === 0) return data;
    
    const lowerQuery = searchQuery.toLowerCase();
    return data.filter(item => {
      return searchKeys.some(key => {
        const val = item[key];
        return val != null && String(val).toLowerCase().includes(lowerQuery);
      });
    });
  });

  let paginatedData = $derived(
    filteredData.slice((currentPage - 1) * perPage, currentPage * perPage)
  );

  // Reset page when search changes
  $effect(() => {
    if (searchQuery !== undefined) {
       currentPage = 1;
    }
  });
</script>

<div class="space-y-4">
  <div class="flex items-center justify-between gap-4">
    {#if searchable && searchKeys.length > 0}
      <div class="relative w-full max-w-sm">
        <Search class="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
        <ShadcnInput
          type="search"
          placeholder={searchPlaceholder}
          class="pl-8 bg-background"
          value={searchQuery}
          oninput={(e) => searchQuery = (e.currentTarget as HTMLInputElement).value}
        />
      </div>
    {/if}

    {#if headerAction}
      <div class="shrink-0">
        {@render headerAction()}
      </div>
    {/if}
  </div>

  <div class="rounded-md border bg-background">
    <ExpandableTable
      {columns}
      rows={paginatedData as Record<string, string | number | boolean | null | undefined>[]}
      {rowKey}
      {emptyText}
      {cell}
      {expanded}
      {header}
    />
  </div>

  {#if filteredData.length > perPage}
    <div class="flex items-center justify-end">
      <Pagination
        count={filteredData.length}
        {perPage}
        bind:page={currentPage}
      />
    </div>
  {/if}
</div>
