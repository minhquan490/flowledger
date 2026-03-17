<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { PhoneInput } from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "phone"]: props.value || "",
    },
  }));
</script>

<PhoneInput {form} name={props.name || "phone"} {...props} />
