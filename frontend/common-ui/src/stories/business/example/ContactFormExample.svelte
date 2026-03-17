<script lang="ts">
  import {AddressForm, EmergencyContactForm} from "../../../lib";

  let {
    type,
    ...rest
  }: { type: "address" | "emergencyContact" } & Record<string, any> = $props();

  const handleAddressSubmit = (data: any) => {
    console.log("[Storybook] Address form submitted:", data);
  };

  const handleEmergencySubmit = (data: any) => {
    console.log("[Storybook] Emergency contact form submitted:", data);
  };
</script>

{#if type === "address"}
  <AddressForm onSubmit={handleAddressSubmit} {...rest} />
{:else}
  <EmergencyContactForm onSubmit={handleEmergencySubmit} {...rest} />
{/if}
