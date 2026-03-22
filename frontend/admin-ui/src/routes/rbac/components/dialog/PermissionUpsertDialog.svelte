<script lang="ts">
  import { Dialog, Select, RadioGroup, PrimaryButton, OutlinedButton } from '@medisphere/common-ui';
  import { LoaderCircle } from '@lucide/svelte';
  import { createForm } from '@tanstack/svelte-form';
  import { z } from 'zod';
  import { useRolesQuery } from '../../hooks/useRoles';
  import { useResourcesQuery } from '../../hooks/useResources';
  import { useUpsertPermissionMutation } from '../../hooks/usePermissions';
  import { toast } from 'svelte-sonner';
  import type { Permission } from '../../types';

  export interface Props {
    open?: boolean;
    initialData?: Permission;
    onClose?: () => void;
  }

  let { open = $bindable(false), initialData, onClose }: Props = $props();

  const rolesQuery = useRolesQuery();
  const resourcesQuery = useResourcesQuery();

  const ACTIONS = ['READ', 'CREATE', 'UPDATE', 'DELETE'] as const;

  const roleOptions = $derived(
    (rolesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );
  const resourceOptions = $derived(
    (resourcesQuery.data ?? []).map((r) => ({ value: r.id, label: r.name }))
  );
  const actionOptions = ACTIONS.map((a) => ({ value: a, label: a }));

  const permissionSchema = z.object({
    roleId: z.string().min(1, 'Role is required'),
    resourceId: z.string().min(1, 'Resource is required'),
    action: z.enum(ACTIONS),
    allowed: z.string().min(1, 'Please choose to allow or deny this permission')
  });

  type PermissionFormValues = z.infer<typeof permissionSchema>;
  type Action = (typeof ACTIONS)[number];

  const upsertMutation = useUpsertPermissionMutation();
  let isSaving = $state(false);

  const form = createForm(() => ({
    defaultValues: {
      roleId: (initialData as unknown as { roleId?: string })?.roleId ?? '',
      resourceId: (initialData as unknown as { resourceId?: string })?.resourceId ?? '',
      action: (initialData?.action as Action) ?? 'READ',
      allowed: initialData ? (initialData.isAllowed ? 'ALLOW' : 'DENY') : ''
    } as unknown as PermissionFormValues,
    validators: {
      onChange: ({ value }) => permissionSchema.safeParse(value).error?.issues[0]?.message
    },
    onSubmit: async ({ value }) => {
      isSaving = true;
      try {
        await upsertMutation.mutateAsync({
          ...initialData,
          roleId: value.roleId,
          resourceId: value.resourceId,
          action: value.action,
          isAllowed: value.allowed === 'ALLOW'
        } as Partial<Permission>);
        toast.success(initialData ? 'Permission updated successfully' : 'Permission created successfully');
        open = false;
        if (onClose) onClose();
      } catch {
        toast.error('Failed to save permission. Please try again.');
      } finally {
        isSaving = false;
      }
    }
  }));

  $effect(() => {
    if (open) {
      if (initialData) {
        form.reset({
          roleId: initialData.roleId ?? '',
          resourceId: initialData.resourceId ?? '',
          action: (initialData.action as Action) ?? 'READ',
          allowed: initialData.isAllowed ? 'ALLOW' : 'DENY'
        } as unknown as PermissionFormValues);
      } else {
        form.reset();
      }
    }
  });
</script>

<Dialog
  bind:open
  title={initialData ? 'Edit Permission' : 'Create Permission'}
  description="Manage role and resource permissions."
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
        {@const roleName = roleOptions.find((r) => r.value === values.roleId)?.label ?? '...'}
        {@const resourceName =
          resourceOptions.find((r) => r.value === values.resourceId)?.label ?? '...'}
        {@const actionName = values.action ?? '...'}
        {@const accessDecision = values.allowed}
        {@const isFormComplete = !!(
          values.roleId &&
          values.resourceId &&
          values.action &&
          values.allowed
        )}

        <div class="space-y-8 pt-4">
          <!-- Section 1: Context -->
          <section class="space-y-4">
            <div class="flex items-center gap-2">
              <div class="h-px flex-1 bg-border"></div>
              <h3 class="text-xs font-semibold tracking-wider text-muted-foreground uppercase">
                Context
              </h3>
              <div class="h-px flex-1 bg-border"></div>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <Select
                formApi={form}
                name="roleId"
                label="Role"
                placeholder="Select a role..."
                options={roleOptions}
                required
              />

              <Select
                formApi={form}
                name="resourceId"
                label="Resource"
                placeholder="Select a resource..."
                options={resourceOptions}
                required
              />
            </div>
          </section>

          <!-- Section 2: Permission (Sentence-driven) -->
          <section class="space-y-4">
            <div class="flex items-center gap-2">
              <div class="h-px flex-1 bg-border"></div>
              <h3 class="text-xs font-semibold tracking-wider text-muted-foreground uppercase">
                Permission
              </h3>
              <div class="h-px flex-1 bg-border"></div>
            </div>

            <div class="rounded-xl border border-dashed border-muted-foreground/20 bg-muted/30 p-6">
              <div
                class="flex flex-wrap items-center justify-center gap-x-4 gap-y-6 text-center text-lg font-medium"
              >
                <div class="flex min-w-[120px] flex-col items-center gap-2 text-muted-foreground">
                  <span class="text-2xl font-bold text-foreground transition-all duration-300"
                    >{roleName}</span
                  >
                  <span
                    class="text-[10px] font-normal tracking-tighter uppercase italic opacity-70 sm:text-xs"
                    >Subject</span
                  >
                </div>

                <span class="mt-1 font-normal text-muted-foreground italic">can</span>

                <div class="w-44">
                  <Select
                    formApi={form}
                    name="action"
                    label="Action"
                    placeholder="Action"
                    options={actionOptions}
                    required
                  />
                </div>

                <div class="flex min-w-[120px] flex-col items-center gap-2 text-muted-foreground">
                  <span class="text-2xl font-bold text-foreground transition-all duration-300"
                    >{resourceName}</span
                  >
                  <span
                    class="text-[10px] font-normal tracking-tighter uppercase italic opacity-70 sm:text-xs"
                    >Object</span
                  >
                </div>

                <span class="mt-1 px-2 text-2xl font-light text-muted-foreground">→</span>

                <div class="flex flex-col items-center gap-2">
                  <RadioGroup
                    {form}
                    name="allowed"
                    orientation="horizontal"
                    options={[
                      { label: 'Allow', value: 'ALLOW' },
                      { label: 'Deny', value: 'DENY' }
                    ]}
                    required
                  />
                  <span
                    class="text-[10px] font-normal tracking-tighter text-muted-foreground uppercase italic opacity-70 sm:text-xs"
                    >Decision</span
                  >
                </div>
              </div>
            </div>
          </section>

          <!-- Live Preview -->
          <div class="rounded-lg border bg-muted/50 p-4 shadow-sm transition-all duration-300">
            <p class="mb-2 text-xs font-bold tracking-widest text-muted-foreground/80 uppercase">
              Live Preview
            </p>
            <div class="flex items-center gap-3">
              <div
                class={`h-2.5 w-2.5 rounded-full transition-all duration-500 ease-in-out ${accessDecision === 'ALLOW' ? 'bg-green-500 shadow-[0_0_12px_rgba(34,197,94,0.7)]' : accessDecision === 'DENY' ? 'bg-red-500 shadow-[0_0_12px_rgba(239,68,68,0.7)]' : 'scale-90 bg-slate-300 opacity-50'}`}
              ></div>
              <p class="text-[15px] leading-relaxed font-medium tracking-tight text-foreground/90">
                {#if isFormComplete}
                  This will <span
                    class={accessDecision === 'ALLOW'
                      ? 'font-extrabold text-green-600'
                      : 'font-extrabold text-red-600'}
                    >{accessDecision === 'ALLOW' ? 'ALLOW' : 'DENY'}</span
                  >
                  <span
                    class="decoration-thickness-2 font-semibold underline decoration-green-500/30 underline-offset-4"
                    >{roleName}</span
                  >
                  to
                  <span class="font-bold text-primary">{actionName}</span>
                  <span
                    class="decoration-thickness-2 font-semibold underline decoration-blue-500/30 underline-offset-4"
                    >{resourceName}</span
                  >
                {:else}
                  <span class="font-normal text-muted-foreground/60 italic"
                    >Please complete the configuration to preview the permission...</span
                  >
                {/if}
              </p>
            </div>
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
                {initialData ? 'Update Permission' : 'Create Permission'}
              {/if}
            </PrimaryButton>
          </div>
        </div>
      {/snippet}
    </form.Subscribe>
  </form>
</Dialog>
