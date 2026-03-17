<script lang="ts" module>
  import { type VariantProps, tv } from "tailwind-variants";

  export const inlineVariants = tv({
    base: "flex flex-row",
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
        stretch: "items-stretch",
        baseline: "items-baseline"
      },
      justify: {
        start: "justify-start",
        center: "justify-center",
        end: "justify-end",
        between: "justify-between"
      },
      wrap: {
        false: "flex-nowrap",
        true: "flex-wrap"
      }
    },
    defaultVariants: {
      gap: "md",
      align: "center",
      justify: "start",
      wrap: false
    }
  });

  export type InlineGap = VariantProps<typeof inlineVariants>["gap"];
  export type InlineAlign = VariantProps<typeof inlineVariants>["align"];
  export type InlineJustify = VariantProps<typeof inlineVariants>["justify"];
  export type InlineWrap = VariantProps<typeof inlineVariants>["wrap"];
</script>

<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    gap?: InlineGap;
    align?: InlineAlign;
    justify?: InlineJustify;
    wrap?: InlineWrap;
    children?: Snippet;
  }

  let {
    class: className,
    gap = "md",
    align = "center",
    justify = "start",
    wrap = false,
    children
  }: Props = $props();
</script>

<div class={cn(inlineVariants({ gap, align, justify, wrap }), className)}>
  {@render children?.()}
</div>
