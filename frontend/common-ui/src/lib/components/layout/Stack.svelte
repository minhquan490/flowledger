<script lang="ts" module>
  import { type VariantProps, tv } from "tailwind-variants";

  export const stackVariants = tv({
    base: "flex flex-col",
    variants: {
      gap: {
        none: "gap-0",
        xs: "gap-1",
        sm: "gap-2",
        md: "gap-4",
        lg: "gap-6",
        xl: "gap-8"
      },
      align: {
        start: "items-start",
        center: "items-center",
        end: "items-end",
        stretch: "items-stretch"
      },
      justify: {
        start: "justify-start",
        center: "justify-center",
        end: "justify-end",
        between: "justify-between"
      }
    },
    defaultVariants: {
      gap: "md",
      align: "stretch",
      justify: "start"
    }
  });

  export type StackGap = VariantProps<typeof stackVariants>["gap"];
  export type StackAlign = VariantProps<typeof stackVariants>["align"];
  export type StackJustify = VariantProps<typeof stackVariants>["justify"];
</script>

<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    gap?: StackGap;
    align?: StackAlign;
    justify?: StackJustify;
    children?: Snippet;
  }

  let {
    class: className,
    gap = "md",
    align = "stretch",
    justify = "start",
    children
  }: Props = $props();
</script>

<div class={cn(stackVariants({ gap, align, justify }), className)}>
  {@render children?.()}
</div>
