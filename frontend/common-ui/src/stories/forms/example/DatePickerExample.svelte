<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {DatePicker} from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "date"]: undefined
    }
  }));
</script>

<DatePicker
  {form}
  name={props.name || "date"}
  {...props}
/>
