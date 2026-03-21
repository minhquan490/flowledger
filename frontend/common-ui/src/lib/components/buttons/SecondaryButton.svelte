<script lang="ts">
	import ButtonBase, { type ButtonBaseProps } from "./ButtonBase.svelte";

	let { variant = "secondary", children, ...restProps }: ButtonBaseProps = $props();
</script>

<ButtonBase {variant} {...restProps}>
	{@render children?.()}
</ButtonBase>
