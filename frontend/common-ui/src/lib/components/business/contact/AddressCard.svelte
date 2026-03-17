<script lang="ts">
  import { AddressSummary } from "./index.js";
  import {
    Card,
    CardHeader,
    CardTitle,
    CardContent,
  } from "../../cards/index.js";
  import { cn } from "../../../utils.js";
  import MapPinIcon from "@lucide/svelte/icons/map-pin";
  import { Badge } from "../../data-display/index.js";

  export interface Props {
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country?: string;
    type?: "Home" | "Billing" | "Mailing" | string;
    primary?: boolean;
    class?: string;
  }

  let {
    addressLine1,
    addressLine2,
    city,
    state,
    postalCode,
    country,
    type,
    primary = false,
    class: className,
  }: Props = $props();
</script>

<Card class={cn("w-full max-w-sm", className)}>
  <CardHeader class="pb-3 flex flex-row items-center justify-between space-y-0">
    <CardTitle class="text-base flex items-center gap-2">
      <MapPinIcon class="h-4 w-4 text-muted-foreground" />
      {type || "Address"}
    </CardTitle>
    {#if primary}
      <Badge variant="secondary" class="font-normal text-xs">Primary</Badge>
    {/if}
  </CardHeader>
  <CardContent class="text-sm">
    <AddressSummary
      {addressLine1}
      {addressLine2}
      {city}
      {state}
      {postalCode}
      {country}
    />
  </CardContent>
</Card>
