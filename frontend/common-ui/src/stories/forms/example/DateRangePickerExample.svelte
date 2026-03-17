<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {DateRangePicker} from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "dateRange"]: undefined
    }
  }));
</script>

<DateRangePicker
  {form}
  name={props.name || "dateRange"}
  {...props}
/>
