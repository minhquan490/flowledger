<script lang="ts">
  import PhoneIcon from "@lucide/svelte/icons/phone";
  import { Badge } from "../../data-display/index.js";
  import { cn } from "../../../utils.js";

  export interface Props {
    phoneNumber: string;
    type?: "Mobile" | "Home" | "Work" | "Fax" | string;
    primary?: boolean;
    icon?: boolean;
    class?: string;
  }

  let {
    phoneNumber,
    type,
    primary = false,
    icon = true,
    class: className,
  }: Props = $props();

  // Basic formatting for a 10-digit US number (optional)
  let formattedNumber = $derived.by(() => {
    if (!phoneNumber) return "";
    const cleaned = ("" + phoneNumber).replace(/\D/g, "");
    const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
    if (match) {
      return "(" + match[1] + ") " + match[2] + "-" + match[3];
    }
    return phoneNumber;
  });
</script>

<div class={cn("flex flex-wrap items-center gap-2", className)}>
  {#if icon}
    <PhoneIcon class="text-muted-foreground h-4 w-4" />
  {/if}

  <a
    href="tel:{phoneNumber}"
    class="hover:underline text-foreground whitespace-nowrap"
  >
    {formattedNumber || phoneNumber}
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
