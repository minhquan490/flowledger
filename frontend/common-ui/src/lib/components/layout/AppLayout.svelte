<script lang="ts">
  import { AppSidebar } from "../business/index.js";
  import PageHeader from "./PageHeader.svelte";
  import type { SidebarItem } from "../navigation/SidebarShell.svelte";
  import type { BreadcrumbEntry } from "../navigation/Breadcrumb.svelte";
  import type { Snippet, Component } from "svelte";

  export interface Props {
    // --- Sidebar Props ---
    appTitle?: string;
    appLogo?: Component;
    sidebarItems?: SidebarItem[];
    sidebarOpen?: boolean;

    // --- PageHeader Props ---
    pageTitle: string;
    pageSubtitle?: string;
    breadcrumbs?: BreadcrumbEntry[];
    headerAction?: Snippet;

    // --- Content Props ---
    children?: Snippet;
  }

  let {
    appTitle = "Application",
    appLogo,
    sidebarItems = [],
    sidebarOpen = $bindable(true),
    pageTitle,
    pageSubtitle,
    breadcrumbs = [],
    headerAction,
    children
  }: Props = $props();
</script>

<AppSidebar
  title={appTitle}
  logo={appLogo}
  items={sidebarItems}
  bind:open={sidebarOpen}
>
  <div class="sticky top-0 z-10 -mx-4 -mt-4 mb-4 px-4 pt-4 pb-2 bg-background/95 backdrop-blur supports-backdrop-filter:bg-background/60">
    <PageHeader
      title={pageTitle}
      subtitle={pageSubtitle}
      {breadcrumbs}
      action={headerAction}
    />
  </div>
  <div class="min-h-0">
    {@render children?.()}
  </div>
</AppSidebar>
