<script lang="ts">
  import {
    Card,
    CardHeader,
    CardTitle,
    CardContent,
  } from "../../cards/index.js";
  import { Phone, Mail, User } from "@lucide/svelte";
  import { cn } from "../../../utils.js";

  export interface Props {
    name: string;
    relationship: string;
    phone?: string;
    email?: string;
    class?: string;
  }

  let { name, relationship, phone, email, class: className }: Props = $props();
</script>

<Card class={cn(className)}>
  <CardHeader class="p-4 pb-0">
    <div class="flex items-center gap-2">
      <User class="text-muted-foreground size-4" />
      <CardTitle class="text-base font-semibold">{name}</CardTitle>
      <span class="text-muted-foreground text-xs">• {relationship}</span>
    </div>
  </CardHeader>
  <CardContent class="space-y-2 p-4 pt-2">
    {#if phone}
      <div class="flex items-center gap-2 text-sm">
        <Phone class="text-muted-foreground size-3.5" />
        <a href="tel:{phone}" class="hover:underline">{phone}</a>
      </div>
    {/if}
    {#if email}
      <div class="flex items-center gap-2 text-sm">
        <Mail class="text-muted-foreground size-3.5" />
        <a href="mailto:{email}" class="hover:underline">{email}</a>
      </div>
    {/if}
  </CardContent>
</Card>
