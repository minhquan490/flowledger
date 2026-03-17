import { createRawSnippet, type Snippet } from "svelte";

export function textSnippet(text: string): Snippet {
  return createRawSnippet(() => ({
    render: () => text
  }));
}

export function clickLogger(label: string): () => void {
  return () => {
    console.log(`[Storybook] ${label} clicked`);
  };
}
