<script lang="ts">
  import {
    Avatar as UIAvatar,
    AvatarFallback,
    AvatarImage
  } from "../ui/avatar/index.js";

  export interface Props {
    src?: string;
    alt?: string;
    fallback?: string;
    class?: string;
  }

  let {
    src,
    alt = "avatar",
    fallback = "NA",
    class: className
  }: Props = $props();
</script>

<UIAvatar class={className}>
  {#if src}
    <AvatarImage {src} {alt} />
  {/if}
  <AvatarFallback>{fallback}</AvatarFallback>
</UIAvatar>
