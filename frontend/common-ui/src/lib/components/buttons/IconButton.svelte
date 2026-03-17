<script lang="ts">
  import ButtonBase from "./ButtonBase.svelte";
  import type { ComponentProps } from "svelte";

  export type IconButtonProps = Omit<
    ComponentProps<typeof ButtonBase>,
    "size"
  > & {
    size?: "icon" | "icon-sm" | "icon-lg";
    ariaLabel?: string;
  };
  let {
    size = "icon",
    ariaLabel,
    children,
    ...restProps
  }: IconButtonProps = $props();
</script>

<ButtonBase {size} aria-label={ariaLabel} {...restProps}>
  {@render children?.()}
</ButtonBase>
