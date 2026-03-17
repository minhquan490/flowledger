import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { EmergencyContactCard } from "../../../lib";

const meta = {
    title: "Business/Contact/EmergencyContactCard",
    component: EmergencyContactCard,
    tags: ["autodocs"],
    args: {
        name: "Jane Doe",
        relationship: "Spouse",
        phoneNumber: "5550199"
    }
} satisfies Meta<typeof EmergencyContactCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactCard \n  name="Jane Doe" \n  relationship="Spouse" \n  phoneNumber="5550199" \n/>`
            }
        }
    }
};

export const WithAlternate: Story = {
    args: {
        alternatePhoneNumber: "5550188"
    },
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactCard \n  name="Jane Doe" \n  relationship="Spouse" \n  phoneNumber="5550199" \n  alternatePhoneNumber="5550188" \n/>`
            }
        }
    }
};
