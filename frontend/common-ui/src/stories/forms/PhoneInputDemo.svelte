<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import {PhoneInput} from "../../lib";

  const form = createForm(() => ({
    defaultValues: { phone: "" },
    onSubmit: async () => {},
  }));
</script>

<form
  class="max-w-sm"
  onsubmit={(e) => {
    e.preventDefault();
    form.handleSubmit();
  }}
>
  <PhoneInput
    formApi={form}
    name="phone"
    label="Phone"
    helperText="Include country code"
    placeholder="+1 (555) 123-4567"
  />
</form>
