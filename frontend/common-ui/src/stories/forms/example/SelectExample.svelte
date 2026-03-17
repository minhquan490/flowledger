<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {Select} from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "selectVal"]: props.value || ""
    }
  }));
</script>

<Select
  {form}
  name={props.name || "selectVal"}
  {...props}
/>
