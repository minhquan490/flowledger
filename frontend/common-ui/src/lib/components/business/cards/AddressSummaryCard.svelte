<script lang="ts">
  import {
    Card,
    CardHeader,
    CardTitle,
    CardContent,
  } from "../../cards/index.js";
  import { AddressSummary } from "../contact";
  import { MapPin } from "@lucide/svelte";
  import { cn } from "../../../utils.js";

  export interface Props {
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country?: string;
    title?: string;
    use?: string;
    class?: string;
  }

  let {
    addressLine1,
    addressLine2,
    city,
    state,
    postalCode,
    country,
    title = "Address",
    use,
    class: className,
  }: Props = $props();
</script>

<Card class={cn(className)}>
  <CardHeader class="p-4 pb-0">
    <div class="flex items-center justify-between gap-2">
      <div class="flex items-center gap-2">
        <MapPin class="text-muted-foreground size-4" />
        <CardTitle class="text-base font-semibold">{title}</CardTitle>
      </div>
      {#if use}
        <span
          class="bg-secondary text-secondary-foreground rounded-full px-2 py-0.5 text-[10px] font-medium uppercase tracking-wider"
        >
          {use}
        </span>
      {/if}
    </div>
  </CardHeader>
  <CardContent class="p-4 pt-2">
    <AddressSummary
      {addressLine1}
      {addressLine2}
      {city}
      {state}
      {postalCode}
      {country}
      format="multiLine"
      class="text-sm"
    />
  </CardContent>
</Card>
