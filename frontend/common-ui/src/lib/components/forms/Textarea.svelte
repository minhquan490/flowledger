<script lang="ts">
  import { Textarea as UITextarea } from "../ui/textarea/index.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    OnTextareaEvent,
    TanStackFieldValidators,
    TanStackShowError,
    TextareaProps
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
    id,
    class: className,
    ...restProps
  }: TextareaProps & {
    form: AnyTanStackFormApi;
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
    id?: string;
  } = $props();

  const onInput = (e: OnTextareaEvent, fieldApi: AnyFieldApi) => {
    fieldApi.handleChange((e.currentTarget as HTMLTextAreaElement).value);
  };
</script>

<TanStackFormField
  {form}
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
