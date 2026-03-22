<script lang="ts">
  import { Dialog, Input, Switch, PrimaryButton, OutlinedButton } from '@medisphere/common-ui';
  import { LoaderCircle } from '@lucide/svelte';
  import { createForm } from '@tanstack/svelte-form';
  import { z } from 'zod';
  import { useUpsertRoleMutation } from '../../hooks/useRoles';
  import { toast } from 'svelte-sonner';
  import type { Role } from '../../types';

  export interface Props {
    open?: boolean;
    initialData?: Role;
    onClose?: () => void;
  }

  let { open = $bindable(false), initialData, onClose }: Props = $props();

  const roleSchema = z.object({
    code: z.string().min(1, 'Code is required'),
    name: z.string().min(1, 'Name is required'),
    isDefaultRole: z.boolean()
  });

  type RoleFormValues = z.infer<typeof roleSchema>;

  const upsertMutation = useUpsertRoleMutation();
  let isSaving = $state(false);

  const form = createForm(() => ({
    defaultValues: {
      code: initialData?.code ?? '',
      name: initialData?.name ?? '',
      isDefaultRole: initialData?.isDefaultRole ?? false
    } as RoleFormValues,
    validators: {
      onChange: ({ value }) => roleSchema.safeParse(value).error?.issues[0]?.message
    },
    onSubmit: async ({ value }) => {
      isSaving = true;
      try {
        await upsertMutation.mutateAsync({
          ...initialData,
          ...value
        });
        toast.success(initialData ? 'Role updated successfully' : 'Role created successfully');
        open = false;
        if (onClose) onClose();
      } catch {
        toast.error('Failed to save role. Please try again.');
      } finally {
        isSaving = false;
      }
    }
  }));

  $effect(() => {
    if (open) {
      if (initialData) {
        form.reset({
          code: initialData.code ?? '',
          name: initialData.name ?? '',
          isDefaultRole: initialData.isDefaultRole ?? false
        } as RoleFormValues);
      } else {
        form.reset();
      }
    }
  });
</script>

<Dialog
  bind:open
  title={initialData ? 'Edit Role' : 'Create Role'}
  description="Manage role properties."
>
  <form
    onsubmit={(e) => {
      e.preventDefault();
      e.stopPropagation();
      form.handleSubmit();
    }}
    class="space-y-4 pt-4"
  >
    <form.Subscribe selector={(s) => s.values}>
      {#snippet children(values)}
        {@const isFormComplete = !!(values.code && values.name)}

        <div class="space-y-6">
          <Input
            formApi={form}
            name="code"
            label="Code"
            placeholder="e.g. SUPER_ADMIN"
            required
            description="Unique identifier code for this role."
          />

          <Input
            formApi={form}
            name="name"
            label="Name"
            placeholder="e.g. Super Administrator"
            required
            description="A human-readable name for this role."
          />

          <div class="rounded-lg border bg-muted/20 p-4 transition-all duration-300">
            <Switch
              formApi={form}
              name="isDefaultRole"
              label="Default Role"
              description="If checked, this role is automatically assigned to new users."
            />
          </div>

          <div class="flex justify-end gap-3 border-t pt-6">
            <OutlinedButton
              type="button"
              onclick={() => {
                open = false;
                if (onClose) onClose();
              }}
            >
              Cancel
            </OutlinedButton>
            <PrimaryButton type="submit" disabled={(upsertMutation.isPending || isSaving) || !isFormComplete}>
              {#if upsertMutation.isPending || isSaving}
                <LoaderCircle class="mr-2 h-4 w-4 animate-spin" />
                Saving...
              {:else}
                {initialData ? 'Update Role' : 'Create Role'}
              {/if}
            </PrimaryButton>
          </div>
        </div>
      {/snippet}
    </form.Subscribe>
  </form>
</Dialog>
