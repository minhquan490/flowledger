<script lang="ts">
  import { Breadcrumb } from "../navigation/index.js";
  import type { BreadcrumbEntry } from "../navigation/Breadcrumb.svelte";
  import type { Snippet } from "svelte";
  import { cn } from "../../utils.js";

  export interface Props {
    title: string;
    subtitle?: string;
    breadcrumbs?: BreadcrumbEntry[];
    action?: Snippet;
    class?: string;
  }

  let { title, subtitle, breadcrumbs = [], action, class: className }: Props = $props();
</script>

<div class={cn("flex flex-col gap-4 pb-4 sm:pb-6", className)}>
  {#if breadcrumbs && breadcrumbs.length > 0}
    <div class="mb-2">
      <Breadcrumb items={breadcrumbs} />
    </div>
  {/if}

  <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
    <div class="flex flex-col gap-1.5">
      <h1 class="text-3xl font-bold tracking-tight text-foreground">{title}</h1>
      {#if subtitle}
        <p class="text-sm text-muted-foreground">{subtitle}</p>
      {/if}
    </div>
    
    {#if action}
      <div class="flex shrink-0 items-center justify-start sm:justify-end gap-2">
        {@render action()}
      </div>
    {/if}
  </div>
</div>
