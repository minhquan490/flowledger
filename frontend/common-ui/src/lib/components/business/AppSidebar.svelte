<script lang="ts">
  import { SidebarShell } from "../navigation/index.js";
  import type { SidebarItem } from "../navigation/SidebarShell.svelte";
  import type { Snippet, Component } from "svelte";

  export interface Props {
    title?: string;
    logo?: Component;
    items?: SidebarItem[];
    open?: boolean;
    children?: Snippet;
  }

  let {
    title = "Application",
    logo,
    items = [],
    open = $bindable(true),
    children
  }: Props = $props();
</script>

<SidebarShell bind:open {title} {logo} {items}>
  {#if children}
    {@render children()}
  {/if}
</SidebarShell>
