<script lang="ts">
  import { ModeWatcher } from "mode-watcher";
  import { mode } from "mode-watcher";
  import type { Snippet } from "svelte";
  import {applyTheme} from "@medisphere/common-ui";

  $effect(() => {
    if (mode.current === "light" || mode.current === "dark") {
      applyTheme(mode.current);
    }
  });

  export interface RootLayoutProps {
    children?: Snippet;
  }

  let { children }: RootLayoutProps = $props();
</script>

<ModeWatcher />
{@render children?.()}
