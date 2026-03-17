<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { Input } from "../../forms/index.js";
  import { cn } from "../../../utils.js";
  import { z } from "zod";
  import { PrimaryButton } from "../../buttons/index.js";

  export interface AddressFormValues {
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country?: string;
  }

  export interface Props {
    defaultValues?: Partial<AddressFormValues>;
    onSubmit?: (data: AddressFormValues) => void | Promise<void>;
    class?: string;
  }

  let { defaultValues, onSubmit, class: className }: Props = $props();

  const form = createForm(() => ({
    defaultValues: {
      addressLine1: defaultValues?.addressLine1 || "",
      addressLine2: defaultValues?.addressLine2 || "",
      city: defaultValues?.city || "",
      state: defaultValues?.state || "",
      postalCode: defaultValues?.postalCode || "",
      country: defaultValues?.country || "US",
    },
    onSubmit: async ({ value }: { value: AddressFormValues }) => {
      if (onSubmit) {
        await onSubmit(value);
      }
    },
  }));

  const requiredString = z.string().min(1, "Required field");
</script>

<form
  {...{ "use:form.handleSubmit": true }}
  class={cn("space-y-4 w-full max-w-md", className)}
>
  <Input
    formApi={form}
    name="addressLine1"
    label="Address Line 1"
    type="text"
    validators={{
      onChange: ({ value }) =>
        requiredString.safeParse(value as string).error?.issues[0].message,
    }}
    required
  />

  <Input
    formApi={form}
    name="addressLine2"
    label="Address Line 2 (Optional)"
    type="text"
  />

  <div class="grid grid-cols-2 gap-4">
    <Input
      formApi={form}
      name="city"
      label="City"
      type="text"
      validators={{
        onChange: ({ value }) =>
          requiredString.safeParse(value as string).error?.issues[0].message,
      }}
      required
    />

    <Input
      formApi={form}
      name="state"
      label="State / Province"
      type="text"
      validators={{
        onChange: ({ value }) =>
          requiredString.safeParse(value as string).error?.issues[0].message,
      }}
      required
    />
  </div>

  <div class="grid grid-cols-2 gap-4">
    <Input
      formApi={form}
      name="postalCode"
      label="Postal Code"
      type="text"
      validators={{
        onChange: ({ value }) =>
          requiredString.safeParse(value as string).error?.issues[0].message,
      }}
      required
    />

    <Input
      formApi={form as any}
      name="country"
      label="Country"
      type="text"
      validators={{
        onChange: ({ value }) =>
          requiredString.safeParse(value as string).error?.issues[0].message,
      }}
      required
    />
  </div>

  <div class="pt-2">
    <PrimaryButton type="submit" disabled={form.state.isSubmitting}>
      {#if form.state.isSubmitting}
        Saving...
      {:else}
        Save Address
      {/if}
    </PrimaryButton>
  </div>
</form>
