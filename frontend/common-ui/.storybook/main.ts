import type { StorybookConfig } from "@storybook/svelte-vite";
import tailwindcss from "@tailwindcss/vite";
import { svelte } from "@sveltejs/vite-plugin-svelte";

const config: StorybookConfig = {
  stories: ["../src/**/*.stories.@(ts|svelte)"],
  addons: ["@storybook/addon-docs"],
  framework: {
    name: "@storybook/svelte-vite",
    options: {
      docgen: false
    }
  },
  viteFinal: async (viteConfig) => {
    const plugins = viteConfig.plugins ?? [];
    return {
      ...viteConfig,
      plugins: [
        ...plugins,
        tailwindcss(),
        svelte({
          compilerOptions: {
            runes: true
          }
        })
      ]
    };
  }
};

export default config;
