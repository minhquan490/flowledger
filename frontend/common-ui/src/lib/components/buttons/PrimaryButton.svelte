<script lang="ts">
	import ButtonBase, { type ButtonBaseProps } from "./ButtonBase.svelte";

	let { variant = "primary", ...restProps }: ButtonBaseProps = $props();
</script>

<ButtonBase {variant} {...restProps} />
