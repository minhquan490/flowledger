<script lang="ts">
  import {
    Sidebar,
    SidebarContent,
    SidebarGroup,
    SidebarGroupContent,
    SidebarHeader,
    SidebarInset,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarProvider,
    SidebarTrigger
  } from "../ui/sidebar/index.js";
  import type { Snippet } from "svelte";

  export interface SidebarItem {
    id: string;
    label: string;
    href?: string;
    active?: boolean;
    onClick?: () => void;
  }

  export interface Props {
    title?: string;
    items?: SidebarItem[];
    open?: boolean;
    collapsible?: "offcanvas" | "icon" | "none";
    children?: Snippet;
  }

  let {
    title = "Navigation",
    items = [],
    open = $bindable(true),
    collapsible = "icon",
    children
  }: Props = $props();
</script>

<SidebarProvider bind:open>
  <Sidebar {collapsible}>
    <SidebarHeader>
      <div class="px-2 py-1 text-sm font-semibold">{title}</div>
    </SidebarHeader>

    <SidebarContent>
      <SidebarGroup>
        <SidebarGroupContent>
          <SidebarMenu>
            {#each items as item (item.id)}
              <SidebarMenuItem>
                <SidebarMenuButton
                  isActive={item.active}
                  onclick={() => {
                    if (item.href) {
                      window.location.href = item.href;
                      return;
                    }
                    item.onClick?.();
                  }}
                  tooltipContent={item.label}
                >
                  <span>{item.label}</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            {/each}
          </SidebarMenu>
        </SidebarGroupContent>
      </SidebarGroup>
    </SidebarContent>
  </Sidebar>

  <SidebarInset>
    <header class="flex h-12 items-center gap-2 border-b px-3">
      <SidebarTrigger />
      <span class="text-sm font-medium">{title}</span>
    </header>
    <main class="p-4">
      {@render children?.()}
    </main>
  </SidebarInset>
</SidebarProvider>
