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
            { id: "f1", resourceId: "1", fieldName: "email", sourceMethodName: "getEmail", fieldType: 'text' },
            { id: "f2", resourceId: "1", fieldName: "status", sourceMethodName: "getStatus", fieldType: 'enum', options: ['active', 'inactive', 'pending'] },
            { id: "f4", resourceId: "1", fieldName: "age", sourceMethodName: "getAge", fieldType: 'number' },
            { id: "f5", resourceId: "1", fieldName: "joinedAt", sourceMethodName: "getJoinedAt", fieldType: 'date' }
          ]
        },
        {
          id: "2", name: "Roles", description: "Manage RBAC roles", createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
          fields: [
            { id: "f3", resourceId: "2", fieldName: "code", sourceMethodName: "getCode", fieldType: 'text' },
            { id: "f6", resourceId: "2", fieldName: "isDefaultRole", sourceMethodName: "getIsDefaultRole", fieldType: 'enum', options: ['true', 'false'] }
          ]
        },
        {
          id: "3", name: "Settings", description: "System configuration", createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
          fields: [
             { id: "f7", resourceId: "3", fieldName: "key", sourceMethodName: "getKey", fieldType: 'text' },
             { id: "f8", resourceId: "3", fieldName: "value", sourceMethodName: "getValue", fieldType: 'text' }
          ]
        }
      ];
    }
  }));
}
