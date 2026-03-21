<script lang="ts">
  import { PageHeader } from "../../lib";
  import type { BreadcrumbEntry } from "../../lib/components/navigation/Breadcrumb.svelte";
  import { PrimaryButton, SecondaryButton } from "../../lib/components/buttons";
  import { Plus, Download } from "@lucide/svelte";

  export interface Props {
    title: string;
    subtitle?: string;
    breadcrumbs?: BreadcrumbEntry[];
  }

  let { title, subtitle, breadcrumbs = [] }: Props = $props();
</script>

<PageHeader {title} {subtitle} {breadcrumbs}>
  {#snippet action()}
    <SecondaryButton>
      <Download class="size-4 mr-2" />
      Export
    </SecondaryButton>
    <PrimaryButton>
      <Plus class="size-4 mr-2" />
      Add User
    </PrimaryButton>
  {/snippet}
</PageHeader>
