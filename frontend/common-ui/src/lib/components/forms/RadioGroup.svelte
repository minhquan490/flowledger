<script lang="ts">
  import {
    RadioGroup as UIRadioGroup,
    RadioGroupItem
  } from "../ui/radio-group/index.js";
  import { Label } from "../ui/label/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    RadioGroupProps,
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
    class: className,
    disabled = false,
    orientation = "vertical"
  }: RadioGroupProps & {
    form: AnyTanStackFormApi;
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();
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
    <UIRadioGroup
      value={String(fieldApi.state.value ?? "")}
      onValueChange={(next) => fieldApi.handleChange(next)}
      class={cn(orientation === "horizontal" ? "flex flex-row gap-4" : "grid gap-3")}
      {disabled}
    >
      {#each options as option (option.value)}
        <div class="flex items-center gap-2">
          <RadioGroupItem
            id={`radio-${fieldApi.name}-${option.value}`}
            value={option.value}
            disabled={option.disabled}
          />
          <Label for={`radio-${fieldApi.name}-${option.value}`}>{option.label}</Label>
        </div>
      {/each}
    </UIRadioGroup>
  {/snippet}
</TanStackFormField>
