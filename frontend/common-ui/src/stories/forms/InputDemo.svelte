<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import {Input} from "../../lib";

  const form = createForm(() => ({
    defaultValues: { fullName: "" },
    onSubmit: async () => {}
  }));
</script>

<form
  class="max-w-sm"
  onsubmit={(e) => {
    e.preventDefault();
    form.handleSubmit();
  }}
>
  <Input
    formApi={form}
    name="fullName"
    label="Full name"
    required={true}
    helperText="Use your legal name"
    placeholder="Jane Doe"
    validators={{
      onChange: ({ value }) => (!value ? "Required" : undefined)
    }}
  />
</form>
