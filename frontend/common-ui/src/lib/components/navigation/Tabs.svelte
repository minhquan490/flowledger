<script lang="ts">
  import {
    Tabs as UITabs,
    TabsContent,
    TabsList,
    TabsTrigger
  } from "../ui/tabs/index.js";
  import type { Snippet } from "svelte";

  export interface TabItem {
    value: string;
    label: string;
    disabled?: boolean;
    content?: Snippet;
  }

  export interface Props {
    class?: string;
    listClass?: string;
    contentClass?: string;
    value?: string;
    items?: TabItem[];
    children?: Snippet;
  }

  let {
    class: className,
    listClass,
    contentClass,
    value = $bindable(""),
    items = [],
    children
  }: Props = $props();

  $effect(() => {
    if (!value && items.length > 0) {
      value = items[0]?.value ?? "";
    }
  });
</script>

<UITabs bind:value class={className}>
  {#if items.length > 0}
    <TabsList class={listClass}>
      {#each items as item}
        <TabsTrigger value={item.value} disabled={item.disabled}>{item.label}</TabsTrigger>
      {/each}
    </TabsList>

    {#each items as item}
      <TabsContent value={item.value} class={contentClass}>
        {#if item.content}
          {@render item.content()}
        {:else}
          <div>{item.label} content</div>
        {/if}
      </TabsContent>
    {/each}
  {:else}
    {@render children?.()}
  {/if}
</UITabs>
