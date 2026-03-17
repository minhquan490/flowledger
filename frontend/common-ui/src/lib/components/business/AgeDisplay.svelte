<script lang="ts">
  export interface Props {
    dateOfBirth: string | Date;
    deceasedDate?: string | Date;
    format?: "full" | "short";
    class?: string;
  }

  let {
    dateOfBirth,
    deceasedDate,
    format = "full",
    class: className,
  }: Props = $props();

  let ageText = $derived.by(() => {
    if (!dateOfBirth) return "";

    try {
      const dob = new Date(dateOfBirth);
      if (isNaN(dob.getTime())) return "";

      const end = deceasedDate ? new Date(deceasedDate) : new Date();
      if (isNaN(end.getTime())) return "";

      let age = end.getFullYear() - dob.getFullYear();
      const m = end.getMonth() - dob.getMonth();
      if (m < 0 || (m === 0 && end.getDate() < dob.getDate())) {
        age--;
      }

      // Handle infants (< 1 year old)
      if (age === 0) {
        let months = m;
        if (end.getDate() < dob.getDate()) {
          months--;
        }
        if (months < 0) months += 12;

        if (months === 0) {
          // Calculate days for neonates
          const diffTime = Math.abs(end.getTime() - dob.getTime());
          const days = Math.floor(diffTime / (1000 * 60 * 60 * 24));
          return format === "short" ? `${days}d` : `${days} days`;
        }
        return format === "short" ? `${months}mo` : `${months} months`;
      }

      return format === "short" ? `${age}y` : `${age} years`;
    } catch {
      return "";
    }
  });
</script>

<span class={className} title={deceasedDate ? "Deceased" : undefined}>
  {ageText}
</span>
