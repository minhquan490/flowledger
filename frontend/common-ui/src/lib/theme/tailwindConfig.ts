import type { Config } from "tailwindcss";
import {palette} from "@medisphere/common-ui/theme/colors";

export const config: Config = {
  darkMode: "class",

  content: ["./src/**/*.{html,js,svelte,ts}"],

  theme: {
    extend: {
      colors: {
        ...palette,

        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",

        primary: "hsl(var(--primary))",
        "primary-foreground": "hsl(var(--primary-foreground))",

        secondary: "hsl(var(--secondary))",
        "secondary-foreground": "hsl(var(--secondary-foreground))",

        border: "hsl(var(--border))",
        input: "hsl(var(--input))",

        muted: "hsl(var(--muted))",
        "muted-foreground": "hsl(var(--muted-foreground))"
      }
    }
  },

  plugins: []
};
