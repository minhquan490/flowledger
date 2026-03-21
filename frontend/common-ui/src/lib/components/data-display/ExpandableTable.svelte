<script lang="ts">
  import {
    ShadcnTable,
    ShadcnTableBody,
    ShadcnTableCell,
    ShadcnTableHead,
    ShadcnTableHeader,
    ShadcnTableRow
  } from "../../index.js";
  import { cn } from "../../utils.js";
  import { ChevronRight } from "@lucide/svelte";
  import { slide } from "svelte/transition";
  import type { Snippet } from "svelte";

  export interface DataTableColumn {
    key: string;
    label: string;
    class?: string;
  }

  export interface Props {
    columns?: DataTableColumn[];
    rows?: Array<Record<string, any>>;
    rowKey?: string;
    class?: string;
    tableClass?: string;
    emptyText?: string;
    expandedRows?: Record<string, boolean>;
    // Snippets
    cell?: Snippet<[{ item: any; column: DataTableColumn; value: any }]>;
    expanded?: Snippet<[{ item: any }]>;
    header?: Snippet<[{ column: DataTableColumn }]>;
  }

  let {
    columns = [],
    rows = [],
    rowKey = "id",
    class: className,
    tableClass,
    emptyText = "No records found",
    expandedRows = $bindable({}),
    cell,
    expanded,
    header
  }: Props = $props();

  function toggleRow(id: string) {
    expandedRows[id] = !expandedRows[id];
  }
</script>

<div class={cn("w-full overflow-x-auto", className)}>
  <ShadcnTable class={tableClass}>
    <ShadcnTableHeader>
      <ShadcnTableRow>
        {#if expanded}
          <ShadcnTableHead class="w-[50px]"></ShadcnTableHead>
        {/if}
        {#each columns as col}
          <ShadcnTableHead class={col.class}>
            {#if header}
              {@render header({ column: col })}
            {:else}
              {col.label}
            {/if}
          </ShadcnTableHead>
        {/each}
      </ShadcnTableRow>
    </ShadcnTableHeader>

    <ShadcnTableBody>
      {#if rows.length === 0}
        <ShadcnTableRow>
          <ShadcnTableCell
            class="text-muted-foreground text-center"
            colspan={columns.length + (expanded ? 1 : 0)}
          >
            {emptyText}
          </ShadcnTableCell>
        </ShadcnTableRow>
      {:else}
        {#each rows as row, rowIdx (row[rowKey] ?? rowIdx)}
          <!-- Main Row -->
          <ShadcnTableRow
            class={cn(expanded ? "cursor-pointer" : "")}
            onclick={() => expanded && toggleRow(String(row[rowKey] ?? rowIdx))}
          >
            {#if expanded}
              <ShadcnTableCell>
                <ChevronRight
                  class={cn(
                    "h-4 w-4 transition-transform duration-200",
                    expandedRows[String(row[rowKey] ?? rowIdx)] ? "rotate-90" : ""
                  )}
                />
              </ShadcnTableCell>
            {/if}
            {#each columns as col}
              <ShadcnTableCell class={col.class}>
                {#if cell}
                  {@render cell({ item: row, column: col, value: row[col.key] })}
                {:else}
                  {row[col.key] ?? "-"}
                {/if}
              </ShadcnTableCell>
            {/each}
          </ShadcnTableRow>

          <!-- Expanded Row -->
          {#if expanded && expandedRows[String(row[rowKey] ?? rowIdx)]}
            <ShadcnTableRow class="bg-muted/50 hover:bg-muted/50">
              <ShadcnTableCell colspan={columns.length + 1} class="border-b-0 p-0">
                <div transition:slide={{ duration: 300 }}>
                  {@render expanded({ item: row })}
                </div>
              </ShadcnTableCell>
            </ShadcnTableRow>
          {/if}
        {/each}
      {/if}
    </ShadcnTableBody>
  </ShadcnTable>
</div>
