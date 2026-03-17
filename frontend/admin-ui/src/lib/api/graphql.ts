export interface SearchRequest {
  model: string;
  fields: string[];
  page?: { offset: number; limit: number };
  criteria?: { field: string; operator: string; value: string }[];
}

export interface SearchResult<T> {
  items: T[];
  total: number;
}

/**
 * Shared GraphQL API utility for generic read/search/mutate operations.
 */
export async function executeSearch<T = Record<string, unknown>>(
  fetch: typeof window.fetch,
  request: SearchRequest
): Promise<SearchResult<T>> {
  const query = `
    query Search($request: SearchRequest!) {
      search(request: $request) {
        items
        total
      }
    }
  `;

  // We are omitting full GraphQL client logic for simplicity, doing raw fetch.
  // We assume the backend accepts graphql queries at /graphql.
  const response = await fetch('/graphql', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    body: JSON.stringify({
      query,
      variables: {
        request: {
          model: request.model,
          fields: request.fields,
          page: request.page ?? { offset: 0, limit: 50 },
          criteria: request.criteria ?? []
        }
      }
    })
  });

  if (!response.ok) {
    throw new Error(`GraphQL request failed: ${response.statusText}`);
  }

  const result = await response.json();
  if (result.errors) {
    throw new Error(`GraphQL errors: ${JSON.stringify(result.errors)}`);
  }

  return result.data.search;
}
