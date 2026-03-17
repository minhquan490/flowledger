<script lang="ts">
  import {
    ContextMenu as UIContextMenu,
    ContextMenuContent,
    ContextMenuItem,
    ContextMenuTrigger
  } from "../ui/context-menu/index.js";

  export interface MenuItem {
    label: string;
    disabled?: boolean;
    onSelect?: () => void;
  }

  export interface Props {
    items?: MenuItem[];
    areaLabel?: string;
  }

  let { items = [], areaLabel = "Right click here" }: Props = $props();
</script>

<UIContextMenu>
  <ContextMenuTrigger>
    <div class="rounded-md border border-dashed p-6 text-sm text-muted-foreground">{areaLabel}</div>
  </ContextMenuTrigger>
  <ContextMenuContent>
    {#each items as item (item.label)}
      <ContextMenuItem disabled={item.disabled} onSelect={item.onSelect}>{item.label}</ContextMenuItem>
    {/each}
  </ContextMenuContent>
</UIContextMenu>
