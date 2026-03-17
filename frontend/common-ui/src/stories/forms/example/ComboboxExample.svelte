<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {
    Combobox, type ComboboxOption,
    type TanStackValidatorAsyncFn,
    type TanStackValidatorFn
  } from "../../../lib";

  let {
    name = "city",
    label,
    required,
    helperText,
    showError,
    validators,
    options = [],
    placeholder,
    searchPlaceholder,
    emptyText,
    class: className,
    contentClass,
    disabled
  }: Partial<{
    value?: string;
    options?: ComboboxOption[];
    placeholder?: string;
    searchPlaceholder?: string;
    emptyText?: string;
    class?: string;
    contentClass?: string;
    disabled?: boolean;
  } & {
    name: string;
    label?: string;
    required?: boolean;
    helperText?: string;
    showError?: "touched" | "always" | "never";
    validators?: {
      onMount?: TanStackValidatorFn;
      onChange?: TanStackValidatorFn;
      onBlur?: TanStackValidatorFn;
      onSubmit?: TanStackValidatorFn;
      onDynamic?: TanStackValidatorFn;
      onChangeAsync?: TanStackValidatorAsyncFn;
      onBlurAsync?: TanStackValidatorAsyncFn;
      onSubmitAsync?: TanStackValidatorAsyncFn;
      onDynamicAsync?: TanStackValidatorAsyncFn;
    };
  }> = $props();

  const form = createForm(() => ({
    defaultValues: {
      [name]: ""
    }
  }));
</script>

<Combobox
  {form}
  {name}
  {label}
  {required}
  {helperText}
  {showError}
  {validators}
  {options}
  {placeholder}
  {searchPlaceholder}
  {emptyText}
  class={className}
  {contentClass}
  {disabled}
/>
