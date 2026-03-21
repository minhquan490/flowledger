import { createQuery } from '@tanstack/svelte-query';
import type { RowCondition } from '../types';

export const ROW_CONDITIONS_QUERY_KEY = 'rowConditions';

export function useRowConditionsQuery() {
  return createQuery(() => ({
    queryKey: [ROW_CONDITIONS_QUERY_KEY],
    queryFn: async (): Promise<RowCondition[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        { 
          id: "1", 
          roleId: "1",
          roleName: "Super Administrator", 
          resourceId: "1",
          resourceName: "Users", 
          conditionJson: '{"email": {"op": "like", "value": "%@medisphere.com"}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        },
        { 
          id: "2", 
          roleId: "2",
          roleName: "Standard User", 
          resourceId: "2",
          resourceName: "Roles", 
          conditionJson: '{"code": {"op": "ne", "value": "SUPER_ADMIN"}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        },
        { 
          id: "3", 
          roleId: "3",
          roleName: "Content Editor", 
          resourceId: "3",
          resourceName: "Settings", 
          conditionJson: '{"category": {"op": "in", "value": ["CMS", "UI"]}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        }
      ];
    }
  }));
}
