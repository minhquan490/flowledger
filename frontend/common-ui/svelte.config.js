import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/package').Options} */
const config = {
  preprocess: vitePreprocess(),
  extensions: ['.svelte']
};

export default config;
