export type ThemeMode = "light" | "dark";

export interface ThemePalette {
  background: string;
  foreground: string;
  primary: string;
  primaryForeground: string;
  border: string;
}

export interface ThemeConfig {
  light: ThemePalette;
  dark: ThemePalette;
}

export const theme: ThemeConfig = {
  light: {
    background: "0 0% 100%",
    foreground: "222 47% 11%",
    primary: "221 83% 53%",
    primaryForeground: "210 40% 98%",
    border: "214 32% 91%"
  },

  dark: {
    background: "222 47% 11%",
    foreground: "210 40% 98%",
    primary: "217 91% 60%",
    primaryForeground: "222 47% 11%",
    border: "217 32% 17%"
  }
};
