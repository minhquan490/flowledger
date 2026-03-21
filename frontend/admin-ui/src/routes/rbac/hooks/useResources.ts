import { createQuery } from '@tanstack/svelte-query';
import type { Resource } from '../types';

export const RESOURCES_QUERY_KEY = 'resources';

export function useResourcesQuery() {
  return createQuery(() => ({
    queryKey: [RESOURCES_QUERY_KEY],
    queryFn: async (): Promise<Resource[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        {
          id: "1", name: "Users", description: "Manage system users", createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
          fields: [
            { id: "f1", resourceId: "1", fieldName: "email", sourceMethodName: "getEmail" },
            { id: "f2", resourceId: "1", fieldName: "status", sourceMethodName: "getStatus" }
          ]
        },
        {
          id: "2", name: "Roles", description: "Manage RBAC roles", createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
          fields: [
            { id: "f3", resourceId: "2", fieldName: "code", sourceMethodName: "getCode" }
          ]
        },
        {
          id: "3", name: "Settings", description: "System configuration", createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
          fields: []
        }
      ];
    }
  }));
}
