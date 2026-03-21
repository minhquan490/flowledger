<script lang="ts">
  import { Switch as UISwitch } from "../ui/switch/index.js";
  import { Label } from "../ui/label/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type { SwitchProps } from "./types.js";

  let {
    formApi,
    name,
    helperText,
    showError,
    validators,
    checked = $bindable(false),
    disabled = false,
    label,
    description,
    class: className,
    id,
    onCheckedChange
  }: SwitchProps = $props();

  const inputId = $derived(id ?? `switch-${Math.random().toString(36).slice(2, 9)}`);
</script>

{#if formApi && name}
  <TanStackFormField
    form={formApi}
    {name}
    label={label}
    helperText={helperText ?? description}
    showError={showError ?? 'never'}
    {validators}
    class={className}
  >
    {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
      <UISwitch
        checked={Boolean(fieldApi.state.value)}
        {disabled}
        id={fieldApi.name}
        onCheckedChange={(next) => {
          fieldApi.handleChange(next);
          if (onCheckedChange) onCheckedChange(next);
        }}
      />
    {/snippet}
  </TanStackFormField>
{:else}
  <div class={cn("flex items-center gap-2", className)}>
    <UISwitch 
      bind:checked 
      {disabled} 
      id={inputId} 
      onCheckedChange={(next) => {
        if (onCheckedChange) onCheckedChange(next);
      }}
    />
    {#if label || description}
      <Label for={inputId} class="grid gap-1">
        {#if label}<span>{label}</span>{/if}
        {#if description}<span class="text-muted-foreground text-xs">{description}</span>{/if}
      </Label>
    {/if}
  </div>
{/if}

