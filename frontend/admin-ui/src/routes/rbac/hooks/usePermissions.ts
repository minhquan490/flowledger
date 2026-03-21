import { createQuery } from '@tanstack/svelte-query';
import type { Permission } from '../types';

export const PERMISSIONS_QUERY_KEY = 'permissions';

export function usePermissionsQuery() {
  return createQuery(() => ({
    queryKey: [PERMISSIONS_QUERY_KEY],
    queryFn: async (): Promise<Permission[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        { id: "1", roleName: "Admin", resource: "User", action: "READ", isAllowed: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "2", roleName: "Admin", resource: "User", action: "WRITE", isAllowed: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
        { id: "3", roleName: "User", resource: "User", action: "READ", isAllowed: true, createdAt: new Date().toISOString(), updatedAt: new Date().toISOString() },
      ];
    }
  }));
}
