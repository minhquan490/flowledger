<script lang="ts">
  import {
    Breadcrumb as UIBreadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbPage,
    BreadcrumbSeparator
  } from "../ui/breadcrumb/index.js";

  export interface BreadcrumbEntry {
    label: string;
    href?: string;
  }

  export interface Props {
    class?: string;
    items: BreadcrumbEntry[];
  }

  let { class: className, items }: Props = $props();
</script>

<UIBreadcrumb class={className}>
  <BreadcrumbList>
    {#each items as item, i}
      <BreadcrumbItem>
        {#if i === items.length - 1}
          <BreadcrumbPage>{item.label}</BreadcrumbPage>
        {:else}
          <BreadcrumbLink href={item.href ?? "#"}>{item.label}</BreadcrumbLink>
        {/if}
      </BreadcrumbItem>
      {#if i < items.length - 1}
        <BreadcrumbSeparator />
      {/if}
    {/each}
  </BreadcrumbList>
</UIBreadcrumb>
