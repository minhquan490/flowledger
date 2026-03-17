import ButtonBase from "./ButtonBase.svelte";
import PrimaryButton from "./PrimaryButton.svelte";
import OutlinedButton from "./OutlinedButton.svelte";
import SecondaryButton from "./SecondaryButton.svelte";
import GhostButton from "./GhostButton.svelte";
import DangerButton from "./DangerButton.svelte";
import IconButton from "./IconButton.svelte";

export {
  ButtonBase,
  PrimaryButton,
  OutlinedButton,
  SecondaryButton,
  GhostButton,
  DangerButton,
  IconButton,
};

// Backward-compatible alias for typo-prone import name
export {OutlinedButton as OutlinedButon};

export {
  type ButtonBaseVariant,
  type ButtonBaseSize,
  type ButtonBaseProps,
  buttonBaseVariants
} from "./ButtonBase.svelte";
