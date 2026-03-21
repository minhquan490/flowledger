<script lang="ts">
  import CalendarIcon from "@lucide/svelte/icons/calendar";
  import type { DateValue } from "@internationalized/date";
  import type { AnyFieldApi } from "@tanstack/form-core";
  import { Button, type ButtonProps } from "../ui/button/index.js";
  import { Calendar } from "../ui/calendar/index.js";
  import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover/index.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import { cn } from "@medisphere/common-ui/utils";
  import type { DateRangePickerProps } from "./types.js";
  import type {
    AnyTanStackFormApi,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";

  let {
    form,
    name,
    startName,
    endName,
    startLabel,
    endLabel,
    required,
    showError,
    validators,
    start = $bindable<DateValue | undefined>(undefined),
    end = $bindable<DateValue | undefined>(undefined),
    class: className,
    startPlaceholder = "Start date",
    endPlaceholder = "End date"
  }: DateRangePickerProps & {
    form?: AnyTanStackFormApi;
    /** Base field name (will use `${name}.start` and `${name}.end` if startName/endName not provided). */
    name?: string;
    startName?: string;
    endName?: string;
    startLabel?: string;
    endLabel?: string;
    required?: boolean;
    showError?: TanStackShowError;
    validators?: {
      start?: TanStackFieldValidators;
      end?: TanStackFieldValidators;
    };
  } = $props();

  let resolvedStartName = $derived(
    (startName ?? (name ? `${name}.start` : undefined)) as string | undefined
  );
  let resolvedEndName = $derived(
    (endName ?? (name ? `${name}.end` : undefined)) as string | undefined
  );

  const sameDateValue = (a: DateValue | undefined, b: DateValue | undefined): boolean => {
    if (a === b) return true;
    if (!a || !b) return false;
    // DateValue implementations provide stable string output; this avoids importing compare utilities.
    return a.toString() === b.toString();
  };

  const readFieldDate = (fieldApi: AnyFieldApi): DateValue | undefined => {
    return fieldApi.state.value as DateValue | undefined;
  };

  const getByPath = (obj: unknown, path: string): unknown => {
    if (!obj) return undefined;
    const parts = path.split(".").filter(Boolean);
    let cur: unknown = obj;
    for (const p of parts) {
      if (!cur || (typeof cur !== "object" && typeof cur !== "function")) return undefined;
      cur = (cur as Record<string, unknown>)[p];
    }
    return cur;
  };

  let valuesSub = $state<{ current: unknown } | undefined>(undefined);
  $effect(() => {
    valuesSub = form ? form.useStore((s) => s.values) : undefined;
  });

  $effect(() => {
    if (!form || !resolvedStartName) return;
    const next = getByPath(valuesSub?.current, resolvedStartName) as DateValue | undefined;
    if (!sameDateValue(start, next)) start = next;
  });

  $effect(() => {
    if (!form || !resolvedEndName) return;
    const next = getByPath(valuesSub?.current, resolvedEndName) as DateValue | undefined;
    if (!sameDateValue(end, next)) end = next;
  });

  $effect(() => {
    if (!form || !resolvedStartName) return;
    const current = form.getFieldValue(resolvedStartName as never) as DateValue | undefined;
    if (!sameDateValue(start, current)) form.setFieldValue(resolvedStartName as never, start as never);
  });

  $effect(() => {
    if (!form || !resolvedEndName) return;
    const current = form.getFieldValue(resolvedEndName as never) as DateValue | undefined;
    if (!sameDateValue(end, current)) form.setFieldValue(resolvedEndName as never, end as never);
  });

  let startOpen = $state(false);
  let endOpen = $state(false);
</script>

<div class={cn("grid gap-2 md:grid-cols-2", className)}>
  {#if form && resolvedStartName}
    <TanStackFormField
      {form}
      name={resolvedStartName ?? ''}
      label={startLabel}
      required={required ?? false}
      showError={showError ?? "touched"}
      validators={validators?.start}
    >
      {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
        {@const current = readFieldDate(fieldApi)}

        <Popover
          open={startOpen}
          onOpenChange={(next: boolean) => {
            startOpen = next;
            if (!next) fieldApi.handleBlur();
          }}
        >
          <PopoverTrigger>
            {#snippet child({ props }: { props: ButtonProps })}
              <Button
                variant="outline"
                class={cn(
                  "w-full justify-start text-left font-normal",
                  !current && "text-muted-foreground"
                )}
                {...props}
              >
                <CalendarIcon />
                {current ? current.toString() : startPlaceholder}
              </Button>
            {/snippet}
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <Calendar
              type="single"
              value={current}
              onValueChange={(next) => {
                fieldApi.handleChange(next);
                fieldApi.handleBlur();
                start = next;
                startOpen = false;
              }}
            />
          </PopoverContent>
        </Popover>
      {/snippet}
    </TanStackFormField>
  {:else}
    <Popover open={startOpen} onOpenChange={(next: boolean) => (startOpen = next)}>
      <PopoverTrigger>
        {#snippet child({ props }: { props: ButtonProps })}
          <Button
            variant="outline"
            class={cn("w-full justify-start text-left font-normal", !start && "text-muted-foreground")}
            {...props}
          >
            <CalendarIcon />
            {start ? start.toString() : startPlaceholder}
          </Button>
        {/snippet}
      </PopoverTrigger>
      <PopoverContent class="w-auto p-0">
        <Calendar
          type="single"
          value={start}
          onValueChange={(next) => {
            start = next;
            startOpen = false;
          }}
        />
      </PopoverContent>
    </Popover>
  {/if}

  {#if form && resolvedEndName}
    <TanStackFormField
      {form}
      name={resolvedEndName ?? ''}
      label={endLabel}
      required={required ?? false}
      showError={showError ?? "touched"}
      validators={validators?.end}
    >
      {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
        {@const current = readFieldDate(fieldApi)}

        <Popover
          open={endOpen}
          onOpenChange={(next: boolean) => {
            endOpen = next;
            if (!next) fieldApi.handleBlur();
          }}
        >
          <PopoverTrigger>
            {#snippet child({ props }: { props: ButtonProps })}
              <Button
                variant="outline"
                class={cn(
                  "w-full justify-start text-left font-normal",
                  !current && "text-muted-foreground"
                )}
                {...props}
              >
                <CalendarIcon />
                {current ? current.toString() : endPlaceholder}
              </Button>
            {/snippet}
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <Calendar
              type="single"
              value={current}
              onValueChange={(next) => {
                fieldApi.handleChange(next);
                fieldApi.handleBlur();
                end = next;
                endOpen = false;
              }}
            />
          </PopoverContent>
        </Popover>
      {/snippet}
    </TanStackFormField>
  {:else}
    <Popover open={endOpen} onOpenChange={(next: boolean) => (endOpen = next)}>
      <PopoverTrigger>
        {#snippet child({ props }: { props: ButtonProps })}
          <Button
            variant="outline"
            class={cn("w-full justify-start text-left font-normal", !end && "text-muted-foreground")}
            {...props}
          >
            <CalendarIcon />
            {end ? end.toString() : endPlaceholder}
          </Button>
        {/snippet}
      </PopoverTrigger>
      <PopoverContent class="w-auto p-0">
        <Calendar
          type="single"
          value={end}
          onValueChange={(next) => {
            end = next;
            endOpen = false;
          }}
        />
      </PopoverContent>
    </Popover>
  {/if}
</div>
