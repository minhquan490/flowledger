import { createQuery, createMutation, useQueryClient } from '@tanstack/svelte-query';
import type { RowCondition } from '../types';

export const ROW_CONDITIONS_QUERY_KEY = 'rowConditions';

/**
 * Hook to fetch row conditions.
 *
 * @return the query object for row conditions
 */
export function useRowConditionsQuery() {
  return createQuery(() => ({
    queryKey: [ROW_CONDITIONS_QUERY_KEY],
    queryFn: async (): Promise<RowCondition[]> => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 500));
      return [
        {
          id: '1',
          roleId: '1',
          roleName: 'Super Administrator',
          resourceId: '1',
          resourceName: 'Users',
          // Single rule: email contains @medisphere.com
          conditionJson: JSON.stringify({
            kind: 'group',
            id: 'g1',
            logicalOp: 'AND',
            children: [
              {
                kind: 'rule',
                id: 'r1',
                field: 'email',
                op: 'like',
                value: '%@medisphere.com'
              }
            ]
          }),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        },
        {
          id: '2',
          roleId: '2',
          roleName: 'Standard User',
          resourceId: '2',
          resourceName: 'Roles',
          // Single rule: code is not SUPER_ADMIN
          conditionJson: JSON.stringify({
            kind: 'group',
            id: 'g2',
            logicalOp: 'AND',
            children: [
              {
                kind: 'rule',
                id: 'r2',
                field: 'code',
                op: 'ne',
                value: 'SUPER_ADMIN'
              }
            ]
          }),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        },
        {
          id: '3',
          roleId: '3',
          roleName: 'Content Editor',
          resourceId: '3',
          resourceName: 'Settings',
          // Nested example: key starts with "cms" AND (value equals "true" OR value equals "1")
          conditionJson: JSON.stringify({
            kind: 'group',
            id: 'g3',
            logicalOp: 'AND',
            children: [
              {
                kind: 'rule',
                id: 'r3',
                field: 'key',
                op: 'sw',
                value: 'cms'
              },
              {
                kind: 'group',
                id: 'g3-nested',
                logicalOp: 'OR',
                children: [
                  {
                    kind: 'rule',
                    id: 'r4',
                    field: 'value',
                    op: 'eq',
                    value: 'true'
                  },
                  {
                    kind: 'rule',
                    id: 'r5',
                    field: 'value',
                    op: 'eq',
                    value: '1'
                  }
                ]
              }
            ]
          }),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
      ];
    }
  }));
}

/**
 * Mutation hook to create or update a row condition.
 *
 * @return the mutation object
 */
export function useUpsertRowConditionMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (data: Partial<RowCondition>) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 1000));
      console.log('Upserting row condition:', data);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ROW_CONDITIONS_QUERY_KEY] });
    }
  }));
}

/**
 * Mutation hook to delete a row condition.
 *
 * @return the mutation object
 */
export function useDeleteRowConditionMutation() {
  const queryClient = useQueryClient();

  return createMutation(() => ({
    mutationFn: async (id: string) => {
      // Mock network delay
      await new Promise((resolve) => setTimeout(resolve, 800));
      console.log('Deleting row condition:', id);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ROW_CONDITIONS_QUERY_KEY] });
    }
  }));
}