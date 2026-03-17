<script lang="ts">
  import {
    Pagination as UIPagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
  } from "../ui/pagination/index.js";

  export interface Props {
    class?: string;
    count: number;
    perPage?: number;
    siblingCount?: number;
    page?: number;
    onPageChange?: (page: number) => void;
  }

  let {
    class: className,
    count,
    perPage = 10,
    siblingCount = 1,
    page = $bindable(1),
    onPageChange
  }: Props = $props();

  type VisibleItem = { type: "page"; value: number; key: string } | { type: "ellipsis"; key: string };

  const totalPages = $derived(Math.max(1, Math.ceil(count / perPage)));

  const visibleItems = $derived.by<VisibleItem[]>(() => {
    const pages = new Set<number>([1, totalPages, page]);

    for (let i = Math.max(1, page - siblingCount); i <= Math.min(totalPages, page + siblingCount); i += 1) {
      pages.add(i);
    }

    const sorted = Array.from(pages).sort((a, b) => a - b);
    const result: VisibleItem[] = [];

    for (let i = 0; i < sorted.length; i += 1) {
      const current = sorted[i];
      const prev = sorted[i - 1];

      if (i > 0 && prev !== undefined && current - prev > 1) {
        result.push({ type: "ellipsis", key: `ellipsis-${prev}-${current}` });
      }

      result.push({ type: "page", value: current, key: `page-${current}` });
    }

    return result;
  });
</script>

<UIPagination
  class={className}
  {count}
  {perPage}
  {siblingCount}
  bind:page
  {onPageChange}
>
  <PaginationContent>
    <PaginationItem>
      <PaginationPrevious />
    </PaginationItem>

    {#each visibleItems as item (item.key)}
      <PaginationItem>
        {#if item.type === "ellipsis"}
          <PaginationEllipsis />
        {:else}
          <PaginationLink page={{ type: "page", value: item.value }} isActive={page === item.value} />
        {/if}
      </PaginationItem>
    {/each}

    <PaginationItem>
      <PaginationNext />
    </PaginationItem>
  </PaginationContent>
</UIPagination>
