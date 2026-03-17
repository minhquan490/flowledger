import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AddressSummary } from "../../../lib";

const meta = {
    title: "Business/Contact/AddressSummary",
    component: AddressSummary,
    tags: ["autodocs"],
    args: {
        addressLine1: "123 Medical Plaza",
        city: "Health City",
        state: "CA",
        postalCode: "90210",
        country: "USA"
    }
} satisfies Meta<typeof AddressSummary>;

export default meta;
type Story = StoryObj<typeof meta>;

export const MultiLine: Story = {
    args: {
        format: "multiLine"
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressSummary \n  addressLine1="123 Medical Plaza" \n  city="Health City" \n  state="CA" \n  postalCode="90210" \n  country="USA" \n  format="multiLine" \n/>`
            }
        }
    }
};

export const SingleLine: Story = {
    args: {
        format: "singleLine"
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressSummary \n  addressLine1="123 Medical Plaza" \n  city="Health City" \n  state="CA" \n  postalCode="90210" \n  country="USA" \n  format="singleLine" \n/>`
            }
        }
    }
};
