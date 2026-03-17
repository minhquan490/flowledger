<script lang="ts">
  import { cn } from "../../../utils.js";

  export interface Props {
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country?: string;
    format?: "singleLine" | "multiLine";
    class?: string;
  }

  let {
    addressLine1,
    addressLine2,
    city,
    state,
    postalCode,
    country,
    format = "multiLine",
    class: className,
  }: Props = $props();

  let singleLineString = $derived.by(() => {
    return [addressLine1, addressLine2, city, state, postalCode, country]
      .filter(Boolean)
      .join(", ");
  });
</script>

{#if format === "singleLine"}
  <span class={cn("text-foreground", className)}>
    {singleLineString}
  </span>
{:else}
  <address class={cn("not-italic text-foreground", className)}>
    <div class="leading-relaxed">
      <div>{addressLine1}</div>
      {#if addressLine2}
        <div>{addressLine2}</div>
      {/if}
      <div>
        {#if city}{city},{/if}
        {#if state}{state}{/if}
        {#if postalCode}{postalCode}{/if}
      </div>
      {#if country}
        <div>{country}</div>
      {/if}
    </div>
  </address>
{/if}
