<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { Textarea } from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "textareaVal"]: props.value || "",
    },
  }));
</script>

<Textarea {form} name={props.name || "textareaVal"} {...props} />
