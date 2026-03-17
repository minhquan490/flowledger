<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {Checkbox} from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "checkbox"]: false
    }
  }));
</script>

<Checkbox
  {form}
  name={props.name || "checkbox"}
  {...props}
/>
