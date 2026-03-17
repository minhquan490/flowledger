import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AddressSummaryCard } from "../../../lib";

const meta = {
    title: "Business/Cards/AddressSummaryCard",
    component: AddressSummaryCard,
    tags: ["autodocs"],
    args: {
        addressLine1: "123 Medical Plaza",
        city: "Health City",
        state: "CA",
        postalCode: "90210",
        country: "USA",
        title: "Home Address",
        use: "home"
    }
} satisfies Meta<typeof AddressSummaryCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<AddressSummaryCard \n  title="Home Address" \n  addressLine1="123 Medical Plaza" \n  city="Health City" \n  state="CA" \n  postalCode="90210" \n  country="USA" \n  use="home" \n/>`
            }
        }
    }
};

export const WithoutUse: Story = {
    args: {
        use: undefined
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressSummaryCard \n  title="Home Address" \n  addressLine1="123 Medical Plaza" \n  city="Health City" \n  state="CA" \n  postalCode="90210" \n  country="USA" \n/>`
            }
        }
    }
};
