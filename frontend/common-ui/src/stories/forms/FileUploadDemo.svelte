<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";

  const form = createForm(() => ({
    defaultValues: { files: undefined as FileList | undefined },
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
  <FileUpload
    {form}
    name="files"
    label="Attachments"
    helperText="PDF/JPG up to 10MB"
    multiple={true}
  />
</form>
