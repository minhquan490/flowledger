<script lang="ts">
  import {
    Select as UISelect,
    SelectContent,
    SelectItem,
    SelectTrigger
  } from "../ui/select/index.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    SelectProps,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";

  let {
    form,
    name,
    label,
    required,
    helperText,
    showError,
    validators,
    options = [],
    placeholder = "Select an option",
    class: className,
    triggerClass,
    contentClass,
    disabled = false
  }: SelectProps & {
    form: AnyTanStackFormApi;
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  const selectType = "single" as const;
</script>

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
    {@const currentValue = String(fieldApi.state.value ?? "")}
    {@const currentLabel = options.find((item) => item.value === currentValue)?.label ?? ""}

    <UISelect
      {disabled}
      type={selectType}
      value={currentValue}
      items={options}
      onValueChange={(next) => fieldApi.handleChange(next)}
      onOpenChange={(open) => {
        if (!open) fieldApi.handleBlur();
      }}
    >
      <SelectTrigger class={triggerClass}>
        {currentLabel || placeholder}
      </SelectTrigger>
      <SelectContent class={contentClass}>
        {#each options as option (option.value)}
          <SelectItem
            value={option.value}
            label={option.label}
            disabled={option.disabled}
          >
            {option.label}
          </SelectItem>
        {/each}
      </SelectContent>
    </UISelect>
  {/snippet}
</TanStackFormField>
