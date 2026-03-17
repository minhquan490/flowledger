<script lang="ts" module>
  import { type VariantProps, tv } from "tailwind-variants";

  export const gridVariants = tv({
    base: "grid",
    variants: {
      cols: {
        1: "grid-cols-1",
        2: "grid-cols-2",
        3: "grid-cols-3",
        4: "grid-cols-4",
        6: "grid-cols-6",
        12: "grid-cols-12"
      },
      gap: {
        none: "gap-0",
        xs: "gap-1",
        sm: "gap-2",
        md: "gap-4",
        lg: "gap-6",
        xl: "gap-8"
      }
    },
    defaultVariants: {
      cols: 3,
      gap: "md"
    }
  });

  export type GridCols = VariantProps<typeof gridVariants>["cols"];
  export type GridGap = VariantProps<typeof gridVariants>["gap"];
</script>

<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    cols?: GridCols;
    gap?: GridGap;
    children?: Snippet;
  }

  let { class: className, cols = 3, gap = "md", children }: Props = $props();
</script>

<div class={cn(gridVariants({ cols, gap }), className)}>
  {@render children?.()}
</div>
