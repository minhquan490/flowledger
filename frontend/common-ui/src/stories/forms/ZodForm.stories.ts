import type { Meta, StoryObj } from "@storybook/svelte-vite";
import ZodFormDemo from './ZodFormDemo.svelte';

const meta = {
  title: "Components/Forms/ZodForm",
  component: ZodFormDemo,
  tags: ["autodocs"]
} satisfies Meta<typeof ZodFormDemo>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<script lang="ts">\n  import { createForm } from \"@tanstack/svelte-form\";\n  import { z } from \"zod\";\n\n  import { PrimaryButton } from \"@medisphere/common-ui/components/buttons\";\n  import { Input, zodFieldValidator } from \"@medisphere/common-ui/components/forms\";\n\n  const fullNameSchema = z.string().min(1, \"Full name is required\");\n  const emailSchema = z.string().email(\"Enter a valid email\");\n\n  const form = createForm(() => ({\n    defaultValues: { fullName: \"\", email: \"\" },\n    onSubmit: async () => {}\n  }));\n</script>\n\n<form onsubmit={async (e) => { e.preventDefault(); await form.validateAllFields(\"submit\"); await form.handleSubmit(); }}>\n  <Input formApi={form} name=\"fullName\" label=\"Full name\" required={true} showError=\"touched\" validators={{ onChange: zodFieldValidator(fullNameSchema), onBlur: zodFieldValidator(fullNameSchema), onSubmit: zodFieldValidator(fullNameSchema) }} />\n  <Input formApi={form} name=\"email\" label=\"Email\" required={true} autocomplete=\"email\" showError=\"touched\" validators={{ onChange: zodFieldValidator(emailSchema), onBlur: zodFieldValidator(emailSchema), onSubmit: zodFieldValidator(emailSchema) }} />\n  <PrimaryButton type=\"submit\">Submit</PrimaryButton>\n</form>`
      }
    }
  }
};
