<script lang="ts">
  import { Switch as UISwitch } from "../ui/switch/index.js";
  import { Label } from "../ui/label/index.js";
  import { cn } from "../../utils.js";
  import TanStackFormField from "./TanStackFormField.svelte";
  import type { SwitchProps } from "./types.js";
  import type {
    AnyTanStackFormApi,
    TanStackFieldValidators,
    TanStackShowError
  } from "./types.js";

  let {
    form,
    name,
    helperText,
    showError,
    validators,
    checked = $bindable(false),
    disabled = false,
    label,
    description,
    class: className,
    id
  }: SwitchProps & {
    form: AnyTanStackFormApi;
    name: string;
    helperText?: string;
    showError?: TanStackShowError;
    validators?: TanStackFieldValidators;
  } = $props();

  const inputId = $derived(id ?? `switch-${Math.random().toString(36).slice(2, 9)}`);
</script>

{#if form && name}
  <TanStackFormField
    {form}
    name={name ?? ''}
    label={label}
    helperText={helperText ?? description}
    showError={showError ?? 'never'}
    {validators}
    class={className}
  >
    {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
      <div class="flex items-center gap-2">
        <UISwitch
          checked={Boolean(fieldApi.state.value)}
          {disabled}
          id={fieldApi.name}
          onCheckedChange={(next) => fieldApi.handleChange(next)}
        />
        {#if label || description}
          <Label for={fieldApi.name} class="grid gap-1">
            {#if label}<span>{label}</span>{/if}
            {#if description}<span class="text-muted-foreground text-xs">{description}</span>{/if}
          </Label>
        {/if}
      </div>
    {/snippet}
  </TanStackFormField>
{:else}
  <div class={cn("flex items-center gap-2", className)}>
    <UISwitch bind:checked {disabled} id={inputId} />
    {#if label || description}
      <Label for={inputId} class="grid gap-1">
        {#if label}<span>{label}</span>{/if}
        {#if description}<span class="text-muted-foreground text-xs">{description}</span>{/if}
      </Label>
    {/if}
  </div>
{/if}
