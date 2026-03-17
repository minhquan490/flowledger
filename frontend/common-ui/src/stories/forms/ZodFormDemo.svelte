<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { z } from "zod";
  import {Input, PrimaryButton, zodFieldValidator} from "../../lib";

  const fullNameSchema = z
    .string()
    .min(1, "Full name is required")
    .max(80, "Full name is too long");

  const emailSchema = z.string().email("Enter a valid email");

  const form = createForm(() => ({
    defaultValues: {
      fullName: "",
      email: "",
    },
    onSubmit: async () => {
      // no-op (storybook)
    },
  }));
</script>

<form
  class="grid max-w-md gap-4"
  onsubmit={async (e) => {
    e.preventDefault();
    // Mark all fields as touched and show submit-time errors.
    await form.validateAllFields("submit");
    await form.handleSubmit();
  }}
>
  <Input
    formApi={form}
    name="fullName"
    label="Full name"
    required={true}
    placeholder="Jane Doe"
    showError="touched"
    validators={{
      onChange: zodFieldValidator(fullNameSchema),
      onBlur: zodFieldValidator(fullNameSchema),
      onSubmit: zodFieldValidator(fullNameSchema),
    }}
  />

  <Input
    formApi={form}
    name="email"
    label="Email"
    required={true}
    placeholder="jane@medisphere.com"
    autocomplete="email"
    inputmode="email"
    showError="touched"
    validators={{
      onChange: zodFieldValidator(emailSchema),
      onBlur: zodFieldValidator(emailSchema),
      onSubmit: zodFieldValidator(emailSchema),
    }}
  />

  <div class="pt-2">
    <PrimaryButton type="submit">Submit</PrimaryButton>
  </div>
</form>
