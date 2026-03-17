import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { TanStackFormField } from "../../lib";

const meta = {
  title: "Components/Forms/TanStackFormField",
  component: TanStackFormField,
  tags: ["autodocs"]
} satisfies Meta<typeof TanStackFormField>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<TanStackFormField
  form={form}
  name="email"
  label="Email"
  required={true}
  helperText="We will never share your email."
  validators={{
    onChange: ({ value }) =>
      !value
        ? "Email is required"
        : String(value).includes("@")
          ? undefined
          : "Enter a valid email"
  }}
>
  {#snippet control(fieldApi: import("@tanstack/form-core").AnyFieldApi)}
    <Input
      id={fieldApi.name}
      name={fieldApi.name}
      value={String(fieldApi.state.value ?? "")}
      oninput={(e: Event) =>
        fieldApi.handleChange((e.target as HTMLInputElement).value)}
      onblur={() => fieldApi.handleBlur()}
      placeholder="you@company.com"
      autocomplete="email"
    />
  {/snippet}
</TanStackFormField>`
      }
    }
  }
};
