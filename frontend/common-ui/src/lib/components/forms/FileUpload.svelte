<script lang="ts">
  import { Input as UIInput } from "../ui/input/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type {
    AnyTanStackFormApi,
    FileInputProps,
    FileUploadProps, HtmlFocusEvent, OnInputEvent,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";
  import type {AnyFieldApi} from "@tanstack/form-core";

  let {
    form,
    name,
    label,
    required,
    showError,
    validators,
    files = $bindable<FileList | undefined>(undefined),
    accept,
    multiple = false,
    disabled = false,
    class: className,
    helperText,
    id,
    ...rest
  }: (FileInputProps & FileUploadProps) & {
    form?: AnyTanStackFormApi;
    name?: string;
    label?: string;
    required?: boolean;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  const onBlur = (e: HtmlFocusEvent, fieldApi: AnyFieldApi) => {
    fieldApi.handleBlur();
    rest.onblur?.(e);
  }

  const onInput = (e: OnInputEvent, fieldApi: AnyFieldApi) => {
    fieldApi.handleChange(
      (e.currentTarget as HTMLInputElement).files ?? undefined
    );
    rest.oninput?.(e);
  }

  const isHasLength = (fieldApi: AnyFieldApi): boolean => {
    return fieldApi.state.value && (fieldApi.state.value as FileList).length > 0;
  }

  const renderFileListLength = (fieldApi: AnyFieldApi): number => {
    return (fieldApi.state.value as FileList).length;
  }
</script>

{#if form && name}
  <TanStackFormField
    {form}
    name={name ?? ''}
    {label}
    required={required ?? false}
    helperText={helperText}
    showError={showError ?? 'never'}
    {validators}
  >
    {#snippet control(fieldApi: AnyFieldApi)}
      <div class={cn("grid gap-1", className)}>
        <UIInput
          {...rest}
          type="file"
          {accept}
          {multiple}
          {disabled}
          id={id ?? fieldApi.name}
          name={fieldApi.name}
          oninput={(e) => onInput(e, fieldApi)}
          onblur={(e) => onBlur(e, fieldApi)}
        />
        {#if isHasLength(fieldApi)}
          <p class="text-xs">
            {renderFileListLength(fieldApi)} file(s) selected
          </p>
        {/if}
      </div>
    {/snippet}
  </TanStackFormField>
{:else}
  <div class={cn("grid gap-1", className)}>
    <UIInput
      {...rest}
      bind:files
      type="file"
      {accept}
      {multiple}
      {disabled}
      {id}
    />
    {#if helperText}
      <p class="text-muted-foreground text-xs">{helperText}</p>
    {/if}
    {#if files && files.length > 0}
      <p class="text-xs">{files.length} file(s) selected</p>
    {/if}
  </div>
{/if}
