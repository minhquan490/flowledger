import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PhoneDisplay } from "../../../lib";

const meta = {
    title: "Business/Contact/PhoneDisplay",
    component: PhoneDisplay,
    tags: ["autodocs"],
    args: {
        phoneNumber: "5551234567",
        type: "Mobile",
        primary: true
    }
} satisfies Meta<typeof PhoneDisplay>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<PhoneDisplay \n  phoneNumber="5551234567" \n  type="Mobile" \n  primary={true} \n/>`
            }
        }
    }
};

export const Work: Story = {
    args: {
        type: "Work",
        primary: false
    },
    parameters: {
        docs: {
            source: {
                code: `<PhoneDisplay \n  phoneNumber="5551234567" \n  type="Work" \n  primary={false} \n/>`
            }
        }
    }
};

export const WithoutIcon: Story = {
    args: {
        icon: false
    },
    parameters: {
        docs: {
            source: {
                code: `<PhoneDisplay \n  phoneNumber="5551234567" \n  icon={false} \n/>`
            }
        }
    }
};
