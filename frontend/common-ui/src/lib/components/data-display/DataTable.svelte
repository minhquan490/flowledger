<script lang="ts">
  import {
    Table as UITable,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
  } from "../ui/table/index.js";
  import { cn } from "../../utils.js";

  export interface DataTableColumn {
    key: string;
    label: string;
    class?: string;
  }

  export interface Props {
    columns?: DataTableColumn[];
    rows?: Array<Record<string, any>>;
    class?: string;
    emptyText?: string;
  }

  let {
    columns = [],
    rows = [],
    class: className,
    emptyText = "No records"
  }: Props = $props();
</script>

<div class={cn("w-full overflow-x-auto", className)}>
  <UITable>
    <TableHeader>
      <TableRow>
        {#each columns as col}
          <TableHead class={col.class}>{col.label}</TableHead>
        {/each}
      </TableRow>
    </TableHeader>

    <TableBody>
      {#if rows.length === 0}
        <TableRow>
          <TableCell class="text-muted-foreground text-center" colspan={Math.max(1, columns.length)}>
            {emptyText}
          </TableCell>
        </TableRow>
      {:else}
        {#each rows as row, rowIdx (`row-${rowIdx}`)}
          <TableRow>
            {#each columns as col, colIdx (`cell-${rowIdx}-${colIdx}`)}
              <TableCell class={col.class}>{row[col.key] ?? "-"}</TableCell>
            {/each}
          </TableRow>
        {/each}
      {/if}
    </TableBody>
  </UITable>
</div>
