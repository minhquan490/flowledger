import { createQuery, createMutation, useQueryClient } from '@tanstack/svelte-query';
import type { Role } from '../types';

export const ROLES_QUERY_KEY = 'roles';

export function useRolesQuery() {
  return createQuery(() => ({
    queryKey: [ROLES_QUERY_KEY],
    queryFn: async (): Promise<Role[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        { id: "1", code: "SUPER_ADMIN", name: "Super Administrator", isDefaultRole: false, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "2", code: "USER", name: "Standard User", isDefaultRole: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "3", code: "EDITOR", name: "Content Editor", isDefaultRole: false, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() }
      ];
    }
  }));
}

/**
 * Mutation hook to create or update a role.
 *
 * @return the mutation object
 */
export function useUpsertRoleMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (data: Partial<Role>) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      console.log('Upserting role:', data);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ROLES_QUERY_KEY] });
    }
  }));
}

/**
 * Mutation hook to delete a role.
 *
 * @return the mutation object
 */
export function useDeleteRoleMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (id: string) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      console.log('Deleting role:', id);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ROLES_QUERY_KEY] });
    }
  }));
}
