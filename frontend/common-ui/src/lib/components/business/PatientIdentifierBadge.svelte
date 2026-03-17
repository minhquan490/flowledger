<script lang="ts">
  import CopyIcon from "@lucide/svelte/icons/copy";
  import { Badge } from "../data-display/index.js";
  import { cn } from "../../utils.js";

  export interface Props {
    identifier: string;
    type?: string;
    copyable?: boolean;
    class?: string;
  }

  let {
    identifier,
    type = "MRN",
    copyable = false,
    class: className,
  }: Props = $props();

  let copied = $state(false);

  async function handleCopy() {
    if (!copyable || !identifier) return;
    try {
      if (typeof navigator !== "undefined" && navigator.clipboard) {
        await navigator.clipboard.writeText(identifier);
      } else {
        // Fallback for older browsers
        const el = document.createElement("textarea");
        el.value = identifier;
        document.body.appendChild(el);
        el.select();
        document.execCommand("copy");
        document.body.removeChild(el);
      }
      copied = true;
      setTimeout(() => {
        copied = false;
      }, 2000);
    } catch (err) {
      console.error("Failed to copy identifier:", err);
    }
  }
</script>

<Badge variant="outline" class={cn("font-mono font-medium", className)}>
  <span class="text-muted-foreground mr-1">{type}:</span>
  <span class="text-foreground">{identifier}</span>

  {#if copyable}
    <button
      type="button"
      onclick={handleCopy}
      class="hover:bg-muted ml-2 inline-flex h-4 w-4 items-center justify-center rounded-sm focus:outline-none"
      aria-label="Copy identifier"
      title={copied ? "Copied!" : "Copy identifier"}
    >
      <CopyIcon class={copied ? "h-3 w-3 text-green-500" : "text-muted-foreground h-3 w-3"} />
    </button>
  {/if}
</Badge>
