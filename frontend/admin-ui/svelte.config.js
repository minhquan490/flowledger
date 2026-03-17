import adapter from '@sveltejs/adapter-auto';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  kit: {
    // adapter-auto only supports some environments, see https://svelte.dev/docs/kit/adapter-auto for a list.
    // If your environment is not supported, or you settled on a specific environment, switch out the adapter.
    // See https://svelte.dev/docs/kit/adapters for more information about adapters.
    adapter: adapter(),
    alias: {
      '@': 'src',
      '@components': 'src/lib/components',
      '@ui': 'src/lib/components/ui',
      '@stores': 'src/lib/stores',
      '@api': 'src/lib/api',
      '@types': 'src/lib/types',
      '@utils': 'src/lib/utils',
      '@constants': 'src/lib/constants',
      '@schemas': 'src/lib/schemas'
    }
  },
  vitePlugin: {
    dynamicCompileOptions: ({ filename }) =>
      filename.includes('node_modules') ? undefined : { runes: true }
  }
};

export default config;
