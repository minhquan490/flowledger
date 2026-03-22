import { createQuery, createMutation, useQueryClient } from '@tanstack/svelte-query';
import type { Permission } from '../types';

export const PERMISSIONS_QUERY_KEY = 'permissions';

export function usePermissionsQuery() {
  return createQuery(() => ({
    queryKey: [PERMISSIONS_QUERY_KEY],
    queryFn: async (): Promise<Permission[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        { id: "1", roleId: "1", roleName: "Super Administrator", resourceId: "1", resource: "Users", action: "READ", isAllowed: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "2", roleId: "1", roleName: "Super Administrator", resourceId: "1", resource: "Users", action: "CREATE", isAllowed: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "3", roleId: "2", roleName: "Standard User", resourceId: "3", resource: "Settings", action: "READ", isAllowed: false, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
      ];
    }
  }));
}

/**
 * Mutation hook to create or update a permission.
 *
 * @return the mutation object
 */
export function useUpsertPermissionMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (data: Partial<Permission>) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      console.log('Upserting permission:', data);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PERMISSIONS_QUERY_KEY] });
    }
  }));
}

/**
 * Mutation hook to delete a permission.
 *
 * @return the mutation object
 */
export function useDeletePermissionMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (id: string) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      console.log('Deleting permission:', id);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PERMISSIONS_QUERY_KEY] });
    }
  }));
}
