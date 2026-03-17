import type { Meta, StoryObj } from "@storybook/svelte-vite";
import type { Address } from "../../../types/business.js";
import { AddressPicker } from "../../../lib";

const mockAddresses: Address[] = [
    { id: "a1", line1: "123 Main St", city: "Springfield", state: "IL", postalCode: "62701", country: "US", use: "home" },
    { id: "a2", line1: "456 Oak Ave", city: "Metropolis", state: "NY", postalCode: "10001", country: "US", use: "work" }
];

const meta = {
    title: "Components/Business/Search/AddressPicker",
    component: AddressPicker,
    tags: ["autodocs"],
    parameters: {
        docs: {
            description: {
                component: "A picker component for selecting an address."
            }
        }
    }
} satisfies Meta<typeof AddressPicker>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        addresses: mockAddresses,
        placeholder: "Select an address...",
        onSelect: (a: Address) => console.log("Selected address:", a)
    },
    parameters: {
        docs: {
            source: {
                code: `<AddressPicker 
  addresses={mockAddresses} 
  placeholder="Select an address..." 
  onSelect={(a) => console.log(a)} 
/>`
            }
        }
    }
};
