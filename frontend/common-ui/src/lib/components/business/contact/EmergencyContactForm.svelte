<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { Input } from "../../forms/index.js";
  import { PrimaryButton } from "../../buttons/index.js";
  import { cn } from "../../../utils.js";
  import { z } from "zod";

  export interface EmergencyContactValues {
    name: string;
    relationship: string;
    phoneNumber: string;
    alternatePhoneNumber?: string;
  }

  export interface Props {
    defaultValues?: Partial<EmergencyContactValues>;
    onSubmit?: (data: EmergencyContactValues) => void | Promise<void>;
    class?: string;
  }

  let { defaultValues, onSubmit, class: className }: Props = $props();

  const form = createForm(() => ({
    defaultValues: {
      name: defaultValues?.name || "",
      relationship: defaultValues?.relationship || "",
      phoneNumber: defaultValues?.phoneNumber || "",
      alternatePhoneNumber: defaultValues?.alternatePhoneNumber || "",
    },
    onSubmit: async ({ value }: { value: EmergencyContactValues }) => {
      if (onSubmit) {
        await onSubmit(value);
      }
    },
  }));

  const requiredString = z.string().min(1, "Required field");
  const phoneValidation = z.string().min(10, "Invalid phone format");

  const onStringChange = (value: unknown) => {
    return requiredString.safeParse(value).error?.issues[0].message;
  }

  const onPhoneChange = (value: unknown) => {
    return phoneValidation.safeParse(value).error?.issues[0].message
  }
</script>

<form
  {...{ "use:form.handleSubmit": true }}
  class={cn("space-y-4 w-full max-w-md", className)}
>
  <Input
    formApi={form}
    name="name"
    label="Contact Name"
    type="text"
    validators={{ onChange: ({ value }) => onStringChange(value) }}
    required
  />

  <Input
    formApi={form}
    name="relationship"
    label="Relationship to Patient"
    type="text"
    placeholder="e.g. Spouse, Parent, Friend"
    validators={{ onChange: ({ value }) => onStringChange(value) }}
    required
  />

  <Input
    formApi={form}
    name="phoneNumber"
    label="Primary Phone Number"
    type="tel"
    validators={{  onChange: ({ value }) => onPhoneChange(value) }}
    required
  />

  <Input
    formApi={form}
    name="alternatePhoneNumber"
    label="Alternate Phone Number"
    type="tel"
  />

  <div class="pt-2">
    <PrimaryButton type="submit" disabled={form.state.isSubmitting}>
      {#if form.state.isSubmitting}
        Saving...
      {:else}
        Save Contact
      {/if}
    </PrimaryButton>
  </div>
</form>
