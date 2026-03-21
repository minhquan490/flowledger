<script lang="ts">
  import { Select as UISelect, SelectContent, SelectItem, SelectTrigger } from "../ui/select/index.js";
  import { TanStackFormField } from "./index.js";
  import type { SelectProps } from "./types.js";

  let {
    formApi,
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
    disabled = false,
    onValueChange,
  }: SelectProps = $props();

  const selectType = "single" as const;
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
        onValueChange={(next: string) => {
          fieldApi.handleChange(next);
          if (onValueChange) onValueChange(next);
        }}
        onOpenChange={(open: boolean) => {
          if (!open) fieldApi.handleBlur();
        }}
      >
        <SelectTrigger class={triggerClass}>
          {currentLabel || placeholder}
        </SelectTrigger>
        <SelectContent class={contentClass}>
          {#each options as option (option.value)}
            <SelectItem value={option.value} label={option.label} disabled={option.disabled}>
              {option.label}
            </SelectItem>
          {/each}
        </SelectContent>
      </UISelect>
    {/snippet}
  </TanStackFormField>
{/if}
