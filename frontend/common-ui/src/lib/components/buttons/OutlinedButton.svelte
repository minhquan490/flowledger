<script lang="ts">
	import ButtonBase, { type ButtonBaseProps } from "./ButtonBase.svelte";

	let { variant = "outline", ...restProps }: ButtonBaseProps = $props();
</script>

<ButtonBase {variant} {...restProps} />
