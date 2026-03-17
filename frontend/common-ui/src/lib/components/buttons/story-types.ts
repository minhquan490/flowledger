export type ButtonSize = "default" | "sm" | "lg" | "icon" | "icon-sm" | "icon-lg";

export type ButtonBaseVariant = "primary" | "outline" | "secondary" | "ghost" | "danger";

export interface BaseButtonStoryArgs {
  size?: ButtonSize;
  disabled?: boolean;
  href?: string;
  onclick?: () => void;
}
