<script lang="ts">
  import { Alert as UIAlert, AlertDescription, AlertTitle } from "../ui/alert/index.js";

  export interface Props {
    title?: string;
    description?: string;
    variant?: "default" | "destructive";
    class?: string;
  }

  let { title = "Notice", description = "This is an alert message.", variant = "default", class: className }: Props = $props();
</script>

<UIAlert {variant} class={className}>
  <AlertTitle>{title}</AlertTitle>
  <AlertDescription>{description}</AlertDescription>
</UIAlert>
