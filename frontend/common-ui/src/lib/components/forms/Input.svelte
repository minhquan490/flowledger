<script lang="ts">
  import { Input as UIInput } from "../ui/input/index.js";
  import { TanStackFormField, FormField } from "./index.js";

  import type { AnyFieldApi } from "@tanstack/form-core";
  import type {
    AnyTanStackFormApi,
    TanStackFieldValidators,
    TanStackShowError,
  } from "./types.js";

  let {
    formApi,
    name,
    label,
    required,
    helperText,
    showError,
    validators,
    id,
    class: className,
    ...rest
  }: {
    formApi?: AnyTanStackFormApi;
    name?: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
    id?: string;
    class?: string;
  } & Record<string, any> = $props();

  const onInput = (e: Event, field: AnyFieldApi) => {
    field.handleChange((e.target as HTMLInputElement).value);
  };

  const onBlur = (_: Event, field: AnyFieldApi) => {
    field.handleBlur();
  };
</script>

{#if formApi && name}
  <TanStackFormField
    form={formApi}
    {name}
    {id}
    {label}
    {required}
    {helperText}
    {validators}
    showError={showError ?? "touched"}
  >
    {#snippet control(field: AnyFieldApi)}
      <UIInput
        {...rest}
        id={id ?? field.name}
        name={field.name}
        class={className}
        value={String(field.state.value ?? "")}
        oninput={(e) => onInput(e, field)}
        onblur={(e) => onBlur(e, field)}
      />
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
    <UIInput {...rest} class={className} {id} />
  </FormField>
{/if}
