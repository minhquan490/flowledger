<script lang="ts" module>
  import { type VariantProps, tv } from "tailwind-variants";

  export const sectionVariants = tv({
    base: "w-full",
    variants: {
      spacing: {
        none: "py-0",
        sm: "py-4",
        md: "py-8",
        lg: "py-12",
        xl: "py-16"
      }
    },
    defaultVariants: {
      spacing: "md"
    }
  });

  export type SectionSpacing = VariantProps<typeof sectionVariants>["spacing"];
</script>

<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    as?: "section" | "div" | "article";
    spacing?: SectionSpacing;
    children?: Snippet;
  }

  let { class: className, as = "section", spacing = "md", children }: Props = $props();
</script>

<svelte:element this={as} class={cn(sectionVariants({ spacing }), className)}>
  {@render children?.()}
</svelte:element>
