<script lang="ts">
  import { Avatar } from "../data-display/index.js";

  export interface Props {
    firstName: string;
    lastName: string;
    src?: string;
    class?: string;
  }

  let {
    firstName = "",
    lastName = "",
    src,
    class: className,
  }: Props = $props();

  let fallback = $derived(
    ((firstName?.[0] || "") + (lastName?.[0] || "")).toUpperCase(),
  );
  let altText = $derived(`${firstName} ${lastName}`);
</script>

<Avatar {src} alt={altText} {fallback} class={className} />
