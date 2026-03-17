<script lang="ts">
  import {
    Card,
    CardHeader,
    CardTitle,
    CardContent,
  } from "../../cards/index.js";
  import { PhoneDisplay } from "./index.js";
  import { Badge } from "../../data-display/index.js";
  import { cn } from "../../../utils.js";
  import UserIcon from "@lucide/svelte/icons/user";

  export interface Props {
    name: string;
    relationship: string;
    phoneNumber: string;
    alternatePhoneNumber?: string;
    class?: string;
  }

  let {
    name,
    relationship,
    phoneNumber,
    alternatePhoneNumber,
    class: className,
  }: Props = $props();
</script>

<Card class={cn("w-full max-w-sm", className)}>
  <CardHeader class="pb-3">
    <div class="flex items-start justify-between">
      <div class="space-y-1">
        <CardTitle class="text-base flex items-center gap-2">
          <UserIcon class="h-4 w-4 text-muted-foreground" />
          {name}
        </CardTitle>
      </div>
      <Badge variant="outline" class="font-normal">{relationship}</Badge>
    </div>
  </CardHeader>
  <CardContent class="space-y-3">
    <PhoneDisplay {phoneNumber} type="Primary" primary={true} />
    {#if alternatePhoneNumber}
      <PhoneDisplay phoneNumber={alternatePhoneNumber} type="Alternate" />
    {/if}
  </CardContent>
</Card>
