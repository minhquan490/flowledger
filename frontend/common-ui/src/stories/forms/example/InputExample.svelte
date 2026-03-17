<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { Input } from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "input"]: props.value || "",
    },
  }));
</script>

<Input formApi={form} name={props.name || "input"} {...props} />
