<script lang="ts">
  import { Checkbox as UICheckbox } from "../ui/checkbox/index.js";
  import { Label } from "../ui/label/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type { CheckboxProps } from "./types.js";
  import type {
    AnyTanStackFormApi,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";

  let {
    formApi,
    name,
    helperText,
    showError,
    validators,
    checked = $bindable(false),
    indeterminate = $bindable(false),
    disabled = false,
    label,
    description,
    class: className,
    id
  }: CheckboxProps = $props();

  const inputId = $derived(id ?? `checkbox-${Math.random().toString(36).slice(2, 9)}`);
</script>

{#if formApi && name}
  <TanStackFormField
    form={formApi}
    name={name}
    label={label}
    helperText={helperText ?? description}
    showError={showError ?? 'never'}
    {validators}
    class={className}
  >
    {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
      <UICheckbox
        checked={Boolean(fieldApi.state.value)}
        indeterminate={indeterminate}
        {disabled}
        id={fieldApi.name}
        onCheckedChange={(next) => fieldApi.handleChange(next)}
      />
    {/snippet}
  </TanStackFormField>
{:else}
  <div class={cn("flex items-start gap-2", className)}>
    <UICheckbox bind:checked bind:indeterminate {disabled} id={inputId} />
    {#if label || description}
      <Label for={inputId} class="grid gap-1">
        {#if label}<span>{label}</span>{/if}
        {#if description}<span class="text-muted-foreground text-xs">{description}</span>{/if}
      </Label>
    {/if}
  </div>
{/if}
