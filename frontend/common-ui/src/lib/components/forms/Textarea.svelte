<script lang="ts">
  import { Textarea as UITextarea } from "../ui/textarea/index.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import FormField from "./FormField.svelte";
  import type {
    AnyTanStackFormApi,
    OnTextareaEvent,
    TanStackFieldValidators,
    TanStackShowError,
    TextareaProps,
  } from "./types.js";
  import type { AnyFieldApi } from "@tanstack/form-core";

  let {
    formApi,
    name,
    label,
    value,
    required,
    helperText,
    showError,
    validators,
    id,
    class: className,
    onValueChange,
    ...restProps
  }: TextareaProps & {
    formApi?: AnyTanStackFormApi;
    name?: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
    id?: string;
    onValueChange?: (value: string) => void;
  } = $props();

  const onInput = (e: OnTextareaEvent, fieldApi: AnyFieldApi) => {
    const val = (e.currentTarget as HTMLTextAreaElement).value;
    fieldApi.handleChange(val);
    if (onValueChange) onValueChange(val);
  };

  const onStandaloneInput = (e: OnTextareaEvent) => {
    const val = (e.currentTarget as HTMLTextAreaElement).value;
    if (onValueChange) onValueChange(val);
  };
</script>

{#if formApi && name}
  <TanStackFormField
    form={formApi}
    {name}
    {label}
    required={required ?? false}
    {helperText}
    showError={showError ?? "never"}
    {validators}
  >
    {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
      <UITextarea
        {...restProps}
        id={id ?? fieldApi.name}
        name={fieldApi.name}
        class={className}
        value={String(fieldApi.state.value ?? "")}
        oninput={(e) => onInput(e, fieldApi)}
        onblur={() => fieldApi.handleBlur()}
      />
    {/snippet}
  </TanStackFormField>
{:else}
  <FormField {id} {label} {required} {helperText} class={className}>
    <UITextarea {...restProps} {id} class={className} {value} oninput={onStandaloneInput} />
  </FormField>
{/if}
