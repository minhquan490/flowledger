export type ResourceField = {
  id: string;
  resourceId: string;
  fieldName: string;
  sourceMethodName: string;
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
  roleName: string;
  resource: string;
  action: string;
  isAllowed: boolean;
  createdAt: string;
  updatedAt: string;
};

export type RowCondition = {
  id: string;
  roleName: string;
  resourceName: string;
  conditionJson: string;
  createdAt: string;
  updatedAt: string;
};
