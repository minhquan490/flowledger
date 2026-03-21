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
          roleName: "Sales Agent", 
          resourceName: "Order", 
          conditionJson: '{"amount": {"op": "lt", "value": 5000}, "status": {"op": "eq", "value": "SUBMITTED"}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        },
        { 
          id: "2", 
          roleName: "Manager", 
          resourceName: "Department", 
          conditionJson: '{"departmentId": {"op": "eq", "value": "SALES"}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        },
        { 
          id: "3", 
          roleName: "HR Specialist", 
          resourceName: "Employee", 
          conditionJson: '{"age": {"op": "between", "value": [18, 65]}, "department": {"op": "in", "value": ["HR", "IT", "Engineering"]}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        },
        { 
          id: "4", 
          roleName: "Auditor", 
          resourceName: "FinancialReport", 
          conditionJson: '{"revenue": {"op": "between", "value": {"from": 10000, "to": 500000}}}', 
          createdAt: new Date().toISOString(), 
          updatedAt: new Date().toISOString() 
        }
      ];
    }
  }));
}
