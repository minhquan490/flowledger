import { theme, type ThemeMode } from "./theme";

export function applyTheme(mode: ThemeMode) {
  if (typeof document === "undefined") {
    return;
  }

  const palette = theme[mode];
  const root = document.documentElement;

  Object.entries(palette).forEach(([key, value]) => {
    root.style.setProperty(`--${key}`, value);
  });
}
