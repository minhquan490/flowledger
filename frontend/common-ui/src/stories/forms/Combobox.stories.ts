import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Combobox } from "../../lib";

const options = [
  { value: "hanoi", label: "Ha Noi" },
  { value: "danang", label: "Da Nang" },
  { value: "hcm", label: "Ho Chi Minh City" }
];

const meta = {
  title: "Components/Forms/Combobox",
  component: Combobox,
  tags: ["autodocs"],
  args: {
    name: "city",
    placeholder: "Select city",
    options
  }
} satisfies Meta<typeof Combobox>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Combobox name="city" placeholder="Select city" options={options} />`
      }
    }
  }
};

export const WithLabel: Story = {
  args: {
    label: "City",
    required: true,
    helperText: "Select your city"
  }
};

export const WithValidation: Story = {
  args: {
    label: "City",
    required: true,
    showError: "touched",
    validators: {
      onBlur: ({ value }: { value: unknown }) =>
        value ? undefined : "City is required"
    }
  }
};

export const Disabled: Story = {
  args: {
    label: "City",
    disabled: true
  }
};
