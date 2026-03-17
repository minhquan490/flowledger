<script lang="ts" module>
  import { type VariantProps, tv } from "tailwind-variants";

  export const containerVariants = tv({
    base: "mx-auto w-full",
    variants: {
      size: {
        sm: "max-w-screen-sm px-4",
        md: "max-w-screen-md px-4",
        lg: "max-w-screen-lg px-4",
        xl: "max-w-screen-xl px-6",
        "2xl": "max-w-screen-2xl px-6",
        full: "max-w-none px-4"
      }
    },
    defaultVariants: {
      size: "xl"
    }
  });

  export type ContainerSize = VariantProps<typeof containerVariants>["size"];
</script>

<script lang="ts">
  import { cn } from "../../utils.js";
  import type { Snippet } from "svelte";

  export interface Props {
    class?: string;
    size?: ContainerSize;
    children?: Snippet;
  }

  let { class: className, size = "xl", children }: Props = $props();
</script>

<div class={cn(containerVariants({ size }), className)}>
  {@render children?.()}
</div>
