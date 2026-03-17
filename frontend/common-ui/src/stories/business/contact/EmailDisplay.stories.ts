import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { EmailDisplay } from "../../../lib";

const meta = {
    title: "Business/Contact/EmailDisplay",
    component: EmailDisplay,
    tags: ["autodocs"],
    args: {
        emailAddress: "john.doe@example.com",
        type: "Personal",
        primary: true
    }
} satisfies Meta<typeof EmailDisplay>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<EmailDisplay \n  emailAddress="john.doe@example.com" \n  type="Personal" \n  primary={true} \n/>`
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
                code: `<EmailDisplay \n  emailAddress="john.doe@example.com" \n  type="Work" \n  primary={false} \n/>`
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
                code: `<EmailDisplay \n  emailAddress="john.doe@example.com" \n  icon={false} \n/>`
            }
        }
    }
};
