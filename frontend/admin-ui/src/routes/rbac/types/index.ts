export type ResourceField = {
  id: string;
  resourceId: string;
  fieldName: string;
  sourceMethodName: string;
  fieldType: 'text' | 'number' | 'date' | 'enum';
  options?: string[];
};

export type Resource = {
  id: string;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
  fields: ResourceField[];
};

export type Role = {
  id: string;
  code: string;
  name: string;
  isDefaultRole: boolean;
  createdAt: string;
  updatedAt: string;
};

export type Permission = {
  id: string;
  roleId?: string;
  resourceId?: string;
  roleName: string;
  resource: string;
  action: string;
  isAllowed: boolean;
  createdAt: string;
  updatedAt: string;
};

// ── Rule tree types (used by RowConditionDialog) ───────────────────────────

export type LogicalOp = 'AND' | 'OR'

export interface RuleLeaf {
  kind: 'rule';
  id: string;
  field: string;
  op: string;
  value: unknown;
}

export interface RuleGroup {
  kind: 'group';
  id: string;
  logicalOp: LogicalOp;
  children: RuleNode[];
}

export type RuleNode = RuleLeaf | RuleGroup;

// ── RowCondition ───────────────────────────────────────────────────────────
// conditionJson stores a serialized RuleGroup tree, e.g.:
// {
//   "kind": "group",
//   "id": "...",
//   "logicalOp": "AND",
//   "children": [
//     { "kind": "rule", "id": "...", "field": "email", "op": "eq", "value": "foo" },
//     {
//       "kind": "group", "id": "...", "logicalOp": "OR",
//       "children": [
//         { "kind": "rule", "id": "...", "field": "status", "op": "eq", "value": "active" }
//       ]
//     }
//   ]
// }

export type RowCondition = {
  id: string;
  roleId: string;
  resourceId: string;
  roleName: string;
  resourceName: string;
  conditionJson: string;
  createdAt: string;
  updatedAt: string;
};