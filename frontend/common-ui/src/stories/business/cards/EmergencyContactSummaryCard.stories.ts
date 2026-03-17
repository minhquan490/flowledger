import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { EmergencyContactSummaryCard } from "../../../lib";

const meta = {
    title: "Business/Cards/EmergencyContactSummaryCard",
    component: EmergencyContactSummaryCard,
    tags: ["autodocs"],
    args: {
        name: "Jane Doe",
        relationship: "Spouse",
        phone: "555-0199",
        email: "jane.doe@example.com"
    }
} satisfies Meta<typeof EmergencyContactSummaryCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactSummaryCard \n  name="Jane Doe" \n  relationship="Spouse" \n  phone="555-0199" \n  email="jane.doe@example.com" \n/>`
            }
        }
    }
};

export const PhoneOnly: Story = {
    args: {
        email: undefined
    },
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactSummaryCard \n  name="Jane Doe" \n  relationship="Spouse" \n  phone="555-0199" \n/>`
            }
        }
    }
};
