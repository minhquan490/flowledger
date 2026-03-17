<script lang="ts">
  import { cn } from "../../../utils.js";
  import { Clock, User } from "@lucide/svelte";

  export interface Props {
    createdBy?: string;
    createdAt?: string | Date;
    updatedBy?: string;
    updatedAt?: string | Date;
    class?: string;
  }

  let {
    createdBy,
    createdAt,
    updatedBy,
    updatedAt,
    class: className,
  }: Props = $props();

  function formatDate(date: string | Date | undefined) {
    if (!date) return "";
    const d = new Date(date);
    if (isNaN(d.getTime())) return "";
    return d.toLocaleString(undefined, {
      dateStyle: "medium",
      timeStyle: "short",
    });
  }
</script>

<div
  class={cn(
    "text-muted-foreground grid grid-cols-1 gap-2 text-xs sm:grid-cols-2",
    className,
  )}
>
  <div class="flex items-center gap-1.5">
    <User class="size-3.5" />
    <span>
      Created by <span class="text-foreground font-medium"
        >{createdBy || "System"}</span
      >
      {#if createdAt}
        on <span class="text-foreground font-medium"
          >{formatDate(createdAt)}</span
        >
      {/if}
    </span>
  </div>
  {#if updatedBy || updatedAt}
    <div class="flex items-center gap-1.5">
      <Clock class="size-3.5" />
      <span>
        Last updated by <span class="text-foreground font-medium"
          >{updatedBy || "System"}</span
        >
        {#if updatedAt}
          on <span class="text-foreground font-medium"
            >{formatDate(updatedAt)}</span
          >
        {/if}
      </span>
    </div>
  {/if}
</div>
