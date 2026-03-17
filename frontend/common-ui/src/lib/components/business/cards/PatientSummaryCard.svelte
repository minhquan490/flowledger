<script lang="ts">
  import { Card, CardContent } from "../../cards/index.js";
  import {
    PatientAvatar,
    PersonName,
    AgeDisplay,
    GenderBadge,
  } from "../index.js";
  import { PatientStatusBadge } from "../status/index.js";
  import { cn } from "../../../utils.js";

  export interface Props {
    firstName: string;
    lastName: string;
    middleName?: string;
    dateOfBirth: string | Date;
    gender: string;
    status: string;
    identifier?: string;
    avatarSrc?: string;
    class?: string;
  }

  let {
    firstName,
    lastName,
    middleName,
    dateOfBirth,
    gender,
    status,
    identifier,
    avatarSrc,
    class: className,
  }: Props = $props();
</script>

<Card class={cn("overflow-hidden", className)}>
  <CardContent class="p-4">
    <div class="flex items-start gap-4">
      <PatientAvatar {firstName} {lastName} src={avatarSrc} class="size-12" />
      <div class="flex-1 space-y-1">
        <div class="flex items-center justify-between gap-2">
          <PersonName
            {firstName}
            {lastName}
            {middleName}
            class="text-lg font-semibold"
          />
          <PatientStatusBadge {status} />
        </div>
        <div
          class="text-muted-foreground flex flex-wrap items-center gap-x-3 gap-y-1 text-sm"
        >
          {#if identifier}
            <span class="font-mono">{identifier}</span>
          {/if}
          <div class="flex items-center gap-1.5">
            <GenderBadge {gender} />
            <span>•</span>
            <AgeDisplay {dateOfBirth} />
          </div>
        </div>
      </div>
    </div>
  </CardContent>
</Card>
