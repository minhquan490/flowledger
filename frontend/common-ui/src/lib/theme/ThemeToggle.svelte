<script lang="ts">
  import { mode, setMode } from "mode-watcher";
  import MoonIcon from "@lucide/svelte/icons/moon";
  import SunIcon from "@lucide/svelte/icons/sun";
  import { IconButton } from "../components/buttons/index.js";
</script>

<IconButton
  variant="outline"
  size="icon"
  ariaLabel={mode.current === "dark" ? "Switch to light mode" : "Switch to dark mode"}
  onclick={() => setMode(mode.current === "dark" ? "light" : "dark")}
>
  {#if mode.current === "dark"}
    <SunIcon class="size-4" />
  {:else}
    <MoonIcon class="size-4" />
  {/if}
</IconButton>
