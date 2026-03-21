<script lang="ts">
  import CalendarIcon from "@lucide/svelte/icons/calendar";
  import type { DateValue } from "@internationalized/date";
  import { Button, type ButtonProps } from "../ui/button/index.js";
  import { Calendar } from "../ui/calendar/index.js";
  import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import Label from "./Label.svelte";
  import HelperText from "./HelperText.svelte";
  import FormField from "./FormField.svelte";
  import type { AnyTanStackFormApi, DatePickerProps, TanStackFieldValidators, TanStackShowError } from "./types.js";
  import type { AnyFieldApi } from "@tanstack/form-core";

  let {
    form,
    name,
    label,
    value,
    required,
    helperText,
    showError,
    validators,
    id,
    placeholder = "Pick a date",
    class: className,
    disabled = false,
    onValueChange,
  }: DatePickerProps & {
    form?: AnyTanStackFormApi;
    name?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  let open = $state(false);

  const readDateCurrent = (fieldApi: AnyFieldApi): DateValue | undefined => {
    return fieldApi.state.value as DateValue | undefined;
  };
</script>

{#if form && name}
  <TanStackFormField
    {form}
    {name}
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
        {open}
        onOpenChange={(next: boolean) => {
          open = next;
          if (!next) fieldApi.handleBlur();
        }}
      >
        <PopoverTrigger>
          {#snippet child({ props }: { props: ButtonProps })}
            <Button
              variant="outline"
              class={cn("w-full justify-start text-left font-normal", !current && "text-muted-foreground")}
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
              if (onValueChange) onValueChange(next);
              open = false;
            }}
          />
        </PopoverContent>
      </Popover>
    {/snippet}
  </TanStackFormField>
{:else}
  <FormField
    {id}
    {label}
    {required}
    {helperText}
    class={className}
  >
    <Popover
      {open}
      onOpenChange={(next: boolean) => {
        open = next;
      }}
    >
      <PopoverTrigger>
        {#snippet child({ props }: { props: ButtonProps })}
          <Button
            variant="outline"
            class={cn("w-full justify-start text-left font-normal", !value && "text-muted-foreground")}
            {disabled}
            {...props}
          >
            <CalendarIcon />
            {value ? value.toString() : placeholder}
          </Button>
        {/snippet}
      </PopoverTrigger>
      <PopoverContent class="w-auto p-0">
        <Calendar
          type="single"
          value={value}
          onValueChange={(next) => {
            if (onValueChange) onValueChange(next);
            open = false;
          }}
        />
      </PopoverContent>
    </Popover>
  </FormField>
{/if}
