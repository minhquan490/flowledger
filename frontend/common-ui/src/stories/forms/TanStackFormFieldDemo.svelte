<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import {Input, TanStackFormField} from "../../lib";

  const form = createForm(() => ({
    defaultValues: {
      email: "",
    },
    onSubmit: async () => {
      // no-op (storybook)
    },
  }));
</script>

<form
  class="max-w-sm"
  onsubmit={(e) => {
    e.preventDefault();
    form.handleSubmit();
  }}
>
  <TanStackFormField
    {form}
    name="email"
    label="Email"
    required={true}
    helperText="We will never share your email."
    validators={{
      onChange: ({ value }) => {
        if (!value) return "Email is required";
        if (!String(value).includes("@")) return "Enter a valid email";
        return undefined;
      },
    }}
  >
    {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
      <Input
        id={fieldApi.name}
        name={fieldApi.name}
        value={String(fieldApi.state.value ?? "")}
        oninput={(e: Event) =>
          fieldApi.handleChange((e.target as HTMLInputElement).value)}
        onblur={() => fieldApi.handleBlur()}
        placeholder="you@company.com"
        autocomplete="email"
      />
    {/snippet}
  </TanStackFormField>
</form>
