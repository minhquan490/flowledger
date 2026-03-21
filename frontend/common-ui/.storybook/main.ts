import { fileURLToPath } from "node:url";
import { dirname } from "node:path";
import type { StorybookConfig } from "@storybook/svelte-vite";
import tailwindcss from "@tailwindcss/vite";
import { svelte } from "@sveltejs/vite-plugin-svelte";

const config: StorybookConfig = {
  stories: ["../src/**/*.stories.@(ts|svelte)"],
  addons: [getAbsolutePath("@storybook/addon-docs")],
  framework: {
    name: getAbsolutePath("@storybook/svelte-vite"),
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

function getAbsolutePath(value: string): any {
  return dirname(fileURLToPath(import.meta.resolve(`${value}/package.json`)));
}
