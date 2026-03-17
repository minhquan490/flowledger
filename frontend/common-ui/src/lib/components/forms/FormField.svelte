<script lang="ts">
  import Label from "./Label.svelte";
  import HelperText from "./HelperText.svelte";
  import ErrorText from "./ErrorText.svelte";
  import { cn } from "../../utils.js";
  import type { FormFieldProps } from "./types.js";

  let {
    id,
    label,
    required = false,
    helperText,
    errorText,
    class: className,
    children
  }: FormFieldProps = $props();
</script>

<div class={cn("grid gap-2", className)}>
  {#if label}
    <Label for={id} {required}>{label}</Label>
  {/if}

  {@render children?.()}

  {#if errorText}
    <ErrorText>{errorText}</ErrorText>
  {:else if helperText}
    <HelperText>{helperText}</HelperText>
  {/if}
</div>
