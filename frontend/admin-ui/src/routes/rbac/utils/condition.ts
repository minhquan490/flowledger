import type { RuleNode, RuleGroup, RuleLeaf, LogicalOp } from '../types';

/**
 * Human-readable labels for operators.
 */
export const OPERATOR_LABELS: Record<string, string> = {
  eq: 'is',
  ne: 'is not',
  gt: '>',
  ge: '>=',
  gte: '>=',
  lt: '<',
  le: '<=',
  lte: '<=',
  like: 'contains',
  sw: 'starts with',
  in: 'is in',
  between: 'is between'
};

/**
 * All operators available in the system.
 */
export const ALL_OPERATORS = [
  { label: 'Equals', value: 'eq' },
  { label: 'Contains', value: 'like' },
  { label: 'Not Equals', value: 'ne' },
  { label: 'Starts With', value: 'sw' },
  { label: '=', value: 'eq' },
  { label: '>', value: 'gt' },
  { label: '<', value: 'lt' },
  { label: '>=', value: 'gte' },
  { label: '<=', value: 'lte' },
  { label: '!=', value: 'ne' },
  { label: 'On', value: 'eq' },
  { label: 'Before', value: 'lt' },
  { label: 'After', value: 'gt' },
  { label: 'Between', value: 'between' },
  { label: 'Is', value: 'eq' },
  { label: 'Is Not', value: 'ne' },
  { label: 'In', value: 'in' }
];

/**
 * Parses condition JSON into a RuleGroup tree.
 * Handles legacy flat formats and the new nested format.
 *
 * @param json The JSON string or object to parse
 * @returns A RuleGroup tree
 */
export function parseConditionJson(json: string | unknown): RuleGroup {
  try {
    const p = (typeof json === 'string' ? JSON.parse(json) : json) as Record<string, unknown> | null;
    if (!p || typeof p !== 'object') return makeDefaultGroup();

    // New nested format
    if (p.kind === 'group') return hydrateGroup(p);

    // Flat array format: { logicalOp, rules: [...] }
    if (Array.isArray(p.rules)) {
      return {
        kind: 'group',
        id: crypto.randomUUID(),
        logicalOp: (p.logicalOp as LogicalOp) ?? 'AND',
        children: (p.rules as Record<string, unknown>[]).map((r) => ({
          kind: 'rule',
          id: crypto.randomUUID(),
          field: String(r.field ?? ''),
          op: String(r.op ?? 'eq'),
          value: r.value ?? ''
        }))
      };
    }

    // Legacy object format: { fieldName: { op, value }, ... }
    const children: RuleLeaf[] = Object.entries(p)
      .filter(([key]) => key !== 'logicalOp' && key !== 'kind' && key !== 'id')
      .map(([field, data]) => {
        const d = data as Record<string, unknown> | null;
        return {
          kind: 'rule',
          id: crypto.randomUUID(),
          field,
          op: String(d?.op ?? 'eq'),
          value: d?.value ?? ''
        };
      });

    return {
      kind: 'group',
      id: crypto.randomUUID(),
      logicalOp: (p.logicalOp as LogicalOp) ?? 'AND',
      children: children.length ? children : []
    };
  } catch {
    return makeDefaultGroup();
  }
}

function hydrateGroup(raw: Record<string, unknown>): RuleGroup {
  const children = Array.isArray(raw.children)
    ? (raw.children as Record<string, unknown>[]).map((child) =>
      child.kind === 'group' ? hydrateGroup(child) : (child as unknown as RuleLeaf)
    )
    : [];
  return {
    kind: 'group',
    id: String(raw.id ?? crypto.randomUUID()),
    logicalOp: (raw.logicalOp as LogicalOp) ?? 'AND',
    children
  };
}

function makeDefaultGroup(): RuleGroup {
  return {
    kind: 'group',
    id: crypto.randomUUID(),
    logicalOp: 'AND',
    children: []
  };
}

/**
 * Converts a RuleNode tree into a human-readable sentence.
 *
 * @param node The RuleNode to convert
 * @returns A human-readable string segment
 */
export function nodeToSentence(node: RuleNode): string {
  if (node.kind === 'rule') {
    if (!node.field || node.value === '' || node.value === undefined) return '…';
    const opLabel =
      ALL_OPERATORS.find((o) => o.value === node.op)?.label ??
      OPERATOR_LABELS[node.op] ??
      node.op;

    if (node.op === 'between' && String(node.value).includes(',')) {
      const [start, end] = String(node.value).split(',');
      return `${node.field} is between "${start}" and "${end}"`;
    }

    return `${node.field} ${opLabel.toLowerCase()} "${node.value}"`;
  }

  const parts = node.children.map(nodeToSentence).filter((s) => s !== '…');
  if (!parts.length) return '…';

  if (parts.length === 1) return parts[0];

  const joined = parts.join(` ${node.logicalOp} `);
  return `(${joined})`;
}

/**
 * Generates the full access logic preview sentence.
 *
 * @param roleName Name of the role
 * @param resourceName Name of the resource
 * @param conditionJson The condition JSON string
 * @returns The full preview sentence
 */
export function getAccessLogicPreview(
  roleName: string,
  resourceName: string,
  conditionJson: string
): string {
  const rootGroup = parseConditionJson(conditionJson);
  const sentence = nodeToSentence(rootGroup);

  if (sentence === '…') {
    return `${roleName} can access all ${resourceName} rows.`;
  }

  return `${roleName} can access ${resourceName} where ${sentence}.`;
}

/**
 * Flattens a RuleGroup tree into a flat list of RuleLeaf nodes.
 * Useful for showing a summary in a table.
 *
 * @param group The RuleGroup to flatten
 * @returns A list of RuleLeaf nodes
 */
export function flattenRules(group: RuleGroup): RuleLeaf[] {
  const leaves: RuleLeaf[] = [];
  function walk(node: RuleNode) {
    if (node.kind === 'rule') {
      leaves.push(node);
    } else {
      node.children.forEach(walk);
    }
  }
  walk(group);
  return leaves;
}
