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
  import type { Snippet, Component } from "svelte";

  export interface SidebarItem {
    id: string;
    label: string;
    href?: string;
    active?: boolean;
    icon?: Component;
    onClick?: () => void;
  }

  export interface Props {
    title?: string;
    logo?: Component;
    items?: SidebarItem[];
    open?: boolean;
    collapsible?: "offcanvas" | "icon" | "none";
    children?: Snippet;
  }

  let {
    title = "Navigation",
    logo,
    items = [],
    open = $bindable(true),
    collapsible = "icon",
    children
  }: Props = $props();
</script>

<SidebarProvider bind:open class="h-svh overflow-hidden">
  <Sidebar {collapsible}>
    <SidebarHeader>
      <SidebarMenu>
        <SidebarMenuItem>
          <SidebarMenuButton size="lg">
            {#if logo}
              {@const LogoIcon = logo}
              <div class="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
                <LogoIcon class="size-4" />
              </div>
            {/if}
            <div class="grid flex-1 text-left text-sm leading-tight">
              <span class="truncate font-semibold">{title}</span>
            </div>
          </SidebarMenuButton>
        </SidebarMenuItem>
      </SidebarMenu>
    </SidebarHeader>

    <SidebarContent>
      <SidebarGroup>
        <SidebarGroupContent>
          <SidebarMenu>
            {#each items as item (item.id)}
              <SidebarMenuItem>
                {#if item.href}
                  <SidebarMenuButton isActive={item.active} tooltipContent={item.label}>
                    {#snippet child({ props })}
                      <a href={item.href} {...props} onclick={(e) => {
                        if (item.onClick) {
                          e.preventDefault();
                          item.onClick();
                        }
                      }}>
                        {#if item.icon}
                          {@const Icon = item.icon}
                          <Icon class="shrink-0 size-4" />
                        {/if}
                        <span>{item.label}</span>
                      </a>
                    {/snippet}
                  </SidebarMenuButton>
                {:else}
                  <SidebarMenuButton
                    isActive={item.active}
                    onclick={() => item.onClick?.()}
                    tooltipContent={item.label}
                  >
                    {#if item.icon}
                      {@const Icon = item.icon}
                      <Icon class="shrink-0 size-4" />
                    {/if}
                    <span>{item.label}</span>
                  </SidebarMenuButton>
                {/if}
              </SidebarMenuItem>
            {/each}
          </SidebarMenu>
        </SidebarGroupContent>
      </SidebarGroup>
    </SidebarContent>
  </Sidebar>

  <SidebarInset class="overflow-x-hidden md:overflow-x-visible">
    <header class="flex h-12 shrink-0 items-center gap-2 border-b px-3">
      <SidebarTrigger />
      <span class="text-sm font-medium">{title}</span>
    </header>
    <main class="flex-1 overflow-auto p-4 pt-0">
      {@render children?.()}
    </main>
  </SidebarInset>
</SidebarProvider>
