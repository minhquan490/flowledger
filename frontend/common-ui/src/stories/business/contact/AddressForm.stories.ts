import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AddressForm } from "../../../lib";

const meta = {
    title: "Business/Contact/AddressForm",
    component: AddressForm,
    tags: ["autodocs"],
    args: {
        onSubmit: (values) => console.log("Form submitted:", values)
    }
} satisfies Meta<typeof AddressForm>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Empty: Story = {
    parameters: {
        docs: {
            source: {
                code: `<AddressForm \n  onSubmit={(values) => console.log(values)} \n/>`
            }
        }
    }
};

export const WithDefaultValues: Story = {
    args: {
        defaultValues: {
            addressLine1: "123 Main St",
            city: "Anytown",
            state: "CA",
            postalCode: "12345",
            country: "US"
        }
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressForm \n  defaultValues={{ \n    addressLine1: "123 Main St", \n    city: "Anytown", \n    state: "CA", \n    postalCode: "12345", \n    country: "US" \n  }} \n  onSubmit={(values) => console.log(values)} \n/>`
            }
        }
    }
};
