<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { FileUpload } from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "files"]: undefined,
    },
  }));
</script>

<FileUpload {form} name={props.name || "files"} {...props} />
