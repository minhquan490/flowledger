<script lang="ts">
  import MailIcon from "@lucide/svelte/icons/mail";
  import { Badge } from "../../data-display";
  import { cn } from "../../../utils.js";

  export interface Props {
    emailAddress: string;
    type?: "Personal" | "Work" | string;
    primary?: boolean;
    icon?: boolean;
    class?: string;
  }

  let {
    emailAddress,
    type,
    primary = false,
    icon = true,
    class: className,
  }: Props = $props();
</script>

<div class={cn("flex flex-wrap items-center gap-2", className)}>
  {#if icon}
    <MailIcon class="text-muted-foreground h-4 w-4" />
  {/if}

  <a
    href="mailto:{emailAddress}"
    class="text-primary hover:underline whitespace-nowrap"
  >
    {emailAddress}
  </a>

  {#if type}
    <span class="text-muted-foreground text-sm">
      {type}
    </span>
  {/if}

  {#if primary}
    <Badge variant="secondary" class="h-5 px-1.5 text-xs font-normal">
      Primary
    </Badge>
  {/if}
</div>
