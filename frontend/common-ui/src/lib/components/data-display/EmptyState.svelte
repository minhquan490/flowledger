<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    title: string;
    description?: string;
    class?: string;
    icon?: Snippet;
    action?: Snippet;
  }

  let { title, description, class: className, icon, action }: Props = $props();
</script>

<div class={cn("bg-card text-card-foreground grid place-items-center gap-2 rounded-lg border p-8 text-center", className)}>
  {#if icon}
    <div class="text-muted-foreground">{@render icon()}</div>
  {/if}
  <h3 class="text-base font-semibold">{title}</h3>
  {#if description}
    <p class="text-muted-foreground text-sm">{description}</p>
  {/if}
  {#if action}
    <div class="mt-2">{@render action()}</div>
  {/if}
</div>
