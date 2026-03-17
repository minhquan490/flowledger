<script lang="ts" module>
	import type {
		ButtonProps as ShadcnButtonProps,
		ButtonSize as ShadcnButtonSize,
		ButtonVariant as ShadcnButtonVariant
	} from "../ui/button/button.svelte";

	export const buttonBaseVariantMap: Record<ButtonBaseVariant, ShadcnButtonVariant> = {
		primary: "default",
		outline: "outline",
		secondary: "secondary",
		ghost: "ghost",
		danger: "destructive"
	};

	export const buttonBaseVariants = {
		variant: ["primary", "outline", "secondary", "ghost", "danger"] as const,
		size: ["default", "sm", "lg", "icon", "icon-sm", "icon-lg"] as const
	};

	export type ButtonBaseVariant = (typeof buttonBaseVariants.variant)[number];
	export type ButtonBaseSize = (typeof buttonBaseVariants.size)[number] & ShadcnButtonSize;
	export type ButtonBaseProps = Omit<ShadcnButtonProps, "variant"> & {
		variant?: ButtonBaseVariant;
		size?: ButtonBaseSize;
	};
</script>

<script lang="ts">
	import ShadcnButton from "../ui/button/button.svelte";

	let {
		class: className,
		variant = "primary",
		size = "default",
		ref = $bindable(null),
		href = undefined,
		type = "button",
		disabled,
		children,
		...restProps
	}: ButtonBaseProps = $props();
</script>

<ShadcnButton
	variant={buttonBaseVariantMap[variant]}
	{size}
	{ref}
	{href}
	{type}
	{disabled}
	class={className}
	{...restProps}
>
	{@render children?.()}
</ShadcnButton>
