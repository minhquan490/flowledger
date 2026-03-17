<script lang="ts">
  import { createForm } from "@tanstack/svelte-form";
  import { Switch } from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "switchVal"]: props.checked || false,
    },
  }));
</script>

<Switch {form} name={props.name || "switchVal"} {...props} />
