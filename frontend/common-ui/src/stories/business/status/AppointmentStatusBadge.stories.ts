import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AppointmentStatusBadge } from "../../../lib";

const meta = {
    title: "Business/Status/AppointmentStatusBadge",
    component: AppointmentStatusBadge,
    tags: ["autodocs"],
    args: {
        status: "scheduled"
    }
} satisfies Meta<typeof AppointmentStatusBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Scheduled: Story = {
    args: { status: "scheduled" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="scheduled" />`
            }
        }
    }
};

export const CheckedIn: Story = {
    args: { status: "checked-in" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="checked-in" />`
            }
        }
    }
};

export const InConsultation: Story = {
    args: { status: "in-consultation" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="in-consultation" />`
            }
        }
    }
};

export const Completed: Story = {
    args: { status: "completed" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="completed" />`
            }
        }
    }
};

export const Cancelled: Story = {
    args: { status: "cancelled" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="cancelled" />`
            }
        }
    }
};

export const NoShow: Story = {
    args: { status: "no-show" },
    parameters: {
        docs: {
            source: {
                code: `<AppointmentStatusBadge status="no-show" />`
            }
        }
    }
};
