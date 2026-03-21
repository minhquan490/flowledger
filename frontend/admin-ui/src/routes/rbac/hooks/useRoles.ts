import { createQuery } from '@tanstack/svelte-query';
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
