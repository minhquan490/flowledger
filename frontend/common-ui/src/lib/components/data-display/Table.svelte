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

  export interface Props {
    headers?: string[];
    rows?: Array<Array<string | number | boolean | null | undefined>>;
    class?: string;
    emptyText?: string;
  }

  let {
    headers = [],
    rows = [],
    class: className,
    emptyText = "No data"
  }: Props = $props();
</script>

<div class={cn("w-full overflow-x-auto", className)}>
  <UITable>
    {#if headers.length > 0}
      <TableHeader>
        <TableRow>
          {#each headers as header}
            <TableHead>{header}</TableHead>
          {/each}
        </TableRow>
      </TableHeader>
    {/if}

    <TableBody>
      {#if rows.length === 0}
        <TableRow>
          <TableCell class="text-muted-foreground text-center" colspan={Math.max(1, headers.length)}>
            {emptyText}
          </TableCell>
        </TableRow>
      {:else}
        {#each rows as row, rowIdx (`row-${rowIdx}`)}
          <TableRow>
            {#each row as cell, colIdx (`cell-${rowIdx}-${colIdx}`)}
              <TableCell>{cell ?? "-"}</TableCell>
            {/each}
          </TableRow>
        {/each}
      {/if}
    </TableBody>
  </UITable>
</div>
