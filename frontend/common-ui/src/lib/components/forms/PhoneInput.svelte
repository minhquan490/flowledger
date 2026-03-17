<script lang="ts">
  import { Input as UIInput } from "../ui/input/index.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    HtmlFocusEvent,
    NonFileInputProps,
    OnInputEvent,
    PhoneInputProps,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";
  import type { AnyFieldApi } from "@tanstack/form-core";

  let {
    formApi,
    name,
    label,
    required,
    helperText,
    showError,
    validators,
    placeholder = "+1 (555) 123-4567",
    class: className,
    disabled = false,
    id,
    ...rest
  }: (NonFileInputProps & PhoneInputProps) & {
    formApi: AnyTanStackFormApi;
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  const onInput = (e: OnInputEvent, fieldApi: AnyFieldApi) => {
    fieldApi.handleChange((e.currentTarget as HTMLInputElement).value);
    rest.oninput?.(e);
  };

  const onBlur = (e: HtmlFocusEvent, fieldApi: AnyFieldApi) => {
    fieldApi.handleBlur();
    rest.onblur?.(e);
  };
</script>

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
    <UIInput
      {...rest}
      type="tel"
      inputmode="tel"
      autocomplete="tel"
      {placeholder}
      class={className}
      {disabled}
      id={id ?? fieldApi.name}
      name={fieldApi.name}
      value={String(fieldApi.state.value ?? "")}
      oninput={(e) => onInput(e, fieldApi)}
      onblur={(e) => onBlur(e, fieldApi)}
    />
  {/snippet}
</TanStackFormField>
