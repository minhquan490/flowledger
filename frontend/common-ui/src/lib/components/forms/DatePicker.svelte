<script lang="ts">
  import CalendarIcon from "@lucide/svelte/icons/calendar";
  import type { DateValue } from "@internationalized/date";
  import { Button } from "../ui/button/index.js";
  import { Calendar } from "../ui/calendar/index.js";
  import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    DatePickerProps,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";
  import type { AnyFieldApi } from "@tanstack/form-core";

  let {
    form,
    name,
    label,
    required,
    helperText,
    showError,
    validators,
    placeholder = "Pick a date",
    class: className,
    disabled = false
  }: DatePickerProps & {
    form: AnyTanStackFormApi;
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  let open = $state(false);

  const readDateCurrent = (fieldApi: AnyFieldApi): DateValue | undefined => {
    return fieldApi.state.value as DateValue | undefined;
  };
</script>

<TanStackFormField
  {form}
  name={name}
  {label}
  required={required ?? false}
  {helperText}
  showError={showError ?? "never"}
  {validators}
  class={className}
>
  {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
    {@const current = readDateCurrent(fieldApi)}
    <Popover
      open={open}
      onOpenChange={(next) => {
        open = next;
        if (!next) fieldApi.handleBlur();
      }}
    >
      <PopoverTrigger>
        {#snippet child({ props })}
          <Button
            variant="outline"
            class={cn(
              "w-full justify-start text-left font-normal",
              !current && "text-muted-foreground"
            )}
            {disabled}
            {...props}
          >
            <CalendarIcon />
            {current ? current.toString() : placeholder}
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
            open = false;
          }}
        />
      </PopoverContent>
    </Popover>
  {/snippet}
</TanStackFormField>
