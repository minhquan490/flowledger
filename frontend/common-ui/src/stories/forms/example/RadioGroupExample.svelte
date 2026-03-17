<script lang="ts">
  import {createForm} from "@tanstack/svelte-form";
  import {RadioGroup} from "../../../lib";

  let props = $props();

  const form = createForm(() => ({
    defaultValues: {
      [props.name || "radioTheme"]: props.value || ""
    }
  }));
</script>

<RadioGroup
  {form}
  name={props.name || "radioTheme"}
  {...props}
/>
