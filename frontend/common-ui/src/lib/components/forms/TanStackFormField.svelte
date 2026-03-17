<script lang="ts" generics="
  TData,
  TFormOnMount extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChange extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChangeAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnBlur extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnBlurAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnSubmit extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnSubmitAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnDynamic extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnDynamicAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnServer extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TSubmitMeta = never
">
  import { Field } from "@tanstack/svelte-form";
  import type {
    AnyFieldApi,
    DeepKeys,
    FormAsyncValidateOrFn,
    FormValidateOrFn
  } from "@tanstack/form-core";

  import type { Snippet } from "svelte";

  import FormField from "./FormField.svelte";
  import type {
    TanStackFieldValidators,
    TanStackShowError,
    TanStackTypedFormApi
  } from "./types.js";

  let {
    form,
    name,
    id,
    label,
    required = false,
    helperText,
    class: className,
    validators,
    showError = "touched",
    control
  } = $props<{
    form: TanStackTypedFormApi<
      TData,
      TFormOnMount,
      TFormOnChange,
      TFormOnChangeAsync,
      TFormOnBlur,
      TFormOnBlurAsync,
      TFormOnSubmit,
      TFormOnSubmitAsync,
      TFormOnDynamic,
      TFormOnDynamicAsync,
      TFormOnServer,
      TSubmitMeta
    >;

    name: DeepKeys<TData>;
    id?: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    class?: string;
    validators?: TanStackFieldValidators;
    showError?: TanStackShowError;

    control?: Snippet<[AnyFieldApi]>;
  }>();

  const formatError = (err: unknown): string => {
    if (!err) return "";
    if (typeof err === "string") return err;
    if (err instanceof Error) return err.message;

    try {
      return JSON.stringify(err);
    } catch {
      return String(err);
    }
  };

  const firstError = (f: AnyFieldApi): string | undefined => {
    const errs = f.state.meta.errors;
    if (!errs?.length) return undefined;
    return formatError(errs[0]) || undefined;
  };

  const resolveError = (field: AnyFieldApi): string | undefined => {
    if (showError === "never") return undefined;

    if (showError === "always") return firstError(field);

    if (showError === "touched" && field.state.meta.isTouched) {
      return firstError(field);
    }

    return undefined;
  };
</script>

<Field {form} {name} {validators}>
  {#snippet children(field: AnyFieldApi)}
    {@const resolvedId = id ?? field.name}
    {@const errorText = resolveError(field)}

    <FormField
      id={resolvedId}
      {label}
      {required}
      helperText={errorText ? undefined : helperText}
      {errorText}
      class={className}
    >
      {#if control}
        {@render control(field)}
      {/if}
    </FormField>
  {/snippet}
</Field>
