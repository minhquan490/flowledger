import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { VerificationStatusBadge } from "../../../lib";

const meta = {
    title: "Business/Status/VerificationStatusBadge",
    component: VerificationStatusBadge,
    tags: ["autodocs"],
    args: {
        status: "verified"
    }
} satisfies Meta<typeof VerificationStatusBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Unverified: Story = {
    args: { status: "unverified" },
    parameters: {
        docs: {
            source: {
                code: `<VerificationStatusBadge status="unverified" />`
            }
        }
    }
};

export const Pending: Story = {
    args: { status: "pending" },
    parameters: {
        docs: {
            source: {
                code: `<VerificationStatusBadge status="pending" />`
            }
        }
    }
};

export const Verified: Story = {
    args: { status: "verified" },
    parameters: {
        docs: {
            source: {
                code: `<VerificationStatusBadge status="verified" />`
            }
        }
    }
};

export const Rejected: Story = {
    args: { status: "rejected" },
    parameters: {
        docs: {
            source: {
                code: `<VerificationStatusBadge status="rejected" />`
            }
        }
    }
};
