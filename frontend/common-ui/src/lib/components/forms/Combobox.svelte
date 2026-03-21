<script lang="ts">
  import CheckIcon from "@lucide/svelte/icons/check";
  import ChevronsUpDownIcon from "@lucide/svelte/icons/chevrons-up-down";
  import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from "../ui/command/index.js";
  import { Button, type ButtonProps } from "../ui/button/index.js";
  import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover/index.js";
  import { cn } from "../../utils.js";
  import { TanStackFormField } from "./index.js";
  import type { AnyFieldApi } from "@tanstack/form-core";
  import type { ComboboxProps } from "./types.js";

  let {
    formApi,
    name,
    label,
    required,
    helperText,
    showError,
    validators,
    options = [],
    placeholder = "Select option",
    searchPlaceholder = "Search...",
    emptyText = "No option found.",
    class: className,
    contentClass,
    disabled = false,
  }: ComboboxProps = $props();

  let open = $state(false);
  let value = $state("");

  function renderCombobox(fieldApi?: AnyFieldApi) {
    const currentValue = fieldApi ? String(fieldApi.state.value ?? "") : value;

    const selected = options.find((o) => o.value === currentValue);

    function select(v: string) {
      if (fieldApi) {
        fieldApi.handleChange(v);
        fieldApi.handleBlur();
      } else {
        value = v;
      }

      open = false;
    }

    return { currentValue, selected, select };
  }
</script>

{#if formApi && name}
  <!-- TanStack mode -->
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
    {#snippet control(fieldApi: AnyFieldApi)}
      {@const ctx = renderCombobox(fieldApi)}

      <Popover
        {open}
        onOpenChange={(next: boolean) => {
          open = next;
          if (!next) fieldApi.handleBlur();
        }}
      >
        <PopoverTrigger>
          {#snippet child({ props }: { props: ButtonProps })}
            <Button
              variant="outline"
              role="combobox"
              aria-expanded={open}
              class={cn("w-full justify-between")}
              {disabled}
              {...props}
            >
              {ctx.selected?.label ?? placeholder}
              <ChevronsUpDownIcon class="opacity-50" />
            </Button>
          {/snippet}
        </PopoverTrigger>

        <PopoverContent class={cn("w-[var(--bits-popover-anchor-width)] p-0", contentClass)}>
          <Command>
            <CommandInput placeholder={searchPlaceholder} />
            <CommandList>
              <CommandEmpty>{emptyText}</CommandEmpty>

              <CommandGroup>
                {#each options as option (option.value)}
                  <CommandItem
                    value={option.value}
                    keywords={option.keywords}
                    disabled={option.disabled}
                    onSelect={() => ctx.select(option.value)}
                  >
                    <CheckIcon class={cn("mr-2", ctx.currentValue === option.value ? "opacity-100" : "opacity-0")} />
                    {option.label}
                  </CommandItem>
                {/each}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    {/snippet}
  </TanStackFormField>
{:else}
  <!-- Standalone mode -->
  {@const ctx = renderCombobox()}

  <Popover {open} onOpenChange={(v: boolean) => (open = v)}>
    <PopoverTrigger>
      {#snippet child({ props }: { props: ButtonProps })}
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          class={cn("w-full justify-between", className)}
          {disabled}
          {...props}
        >
          {ctx.selected?.label ?? placeholder}
          <ChevronsUpDownIcon class="opacity-50" />
        </Button>
      {/snippet}
    </PopoverTrigger>

    <PopoverContent class={cn("w-[var(--bits-popover-anchor-width)] p-0", contentClass)}>
      <Command>
        <CommandInput placeholder={searchPlaceholder} />
        <CommandList>
          <CommandEmpty>{emptyText}</CommandEmpty>

          <CommandGroup>
            {#each options as option (option.value)}
              <CommandItem
                value={option.value}
                keywords={option.keywords}
                disabled={option.disabled}
                onSelect={() => ctx.select(option.value)}
              >
                <CheckIcon class={cn("mr-2", ctx.currentValue === option.value ? "opacity-100" : "opacity-0")} />
                {option.label}
              </CommandItem>
            {/each}
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
{/if}
