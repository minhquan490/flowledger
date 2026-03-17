import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AddressCard } from "../../../lib";

const meta = {
    title: "Business/Contact/AddressCard",
    component: AddressCard,
    tags: ["autodocs"],
    args: {
        addressLine1: "123 Main St",
        city: "Anytown",
        state: "CA",
        postalCode: "12345",
        type: "Home",
        primary: true
    }
} satisfies Meta<typeof AddressCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<AddressCard \n  addressLine1="123 Main St" \n  city="Anytown" \n  state="CA" \n  postalCode="12345" \n  type="Home" \n  primary={true} \n/>`
            }
        }
    }
};

export const Billing: Story = {
    args: {
        type: "Billing",
        primary: false
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressCard \n  addressLine1="123 Main St" \n  city="Anytown" \n  state="CA" \n  postalCode="12345" \n  type="Billing" \n  primary={false} \n/>`
            }
        }
    }
};
