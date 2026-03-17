<script lang="ts">
  export interface Props {
    firstName: string;
    lastName: string;
    middleName?: string;
    prefix?: string;
    suffix?: string;
    format?: "lastFirst" | "firstLast" | "short";
    class?: string;
  }

  let {
    firstName,
    lastName,
    middleName,
    prefix,
    suffix,
    format = "lastFirst",
    class: className,
  }: Props = $props();

  let formattedName = $derived.by(() => {
    switch (format) {
      case "firstLast":
        return [prefix, firstName, middleName, lastName, suffix]
          .filter(Boolean)
          .join(" ");
      case "short":
        return [firstName, lastName].filter(Boolean).join(" ");
      case "lastFirst":
      default:
        const firstPart = [lastName + ","];
        const secondPart = [prefix, firstName, middleName, suffix].filter(
          Boolean,
        );
        return [...firstPart, ...secondPart].join(" ").trim();
    }
  });
</script>

<span class={className}>{formattedName}</span>
