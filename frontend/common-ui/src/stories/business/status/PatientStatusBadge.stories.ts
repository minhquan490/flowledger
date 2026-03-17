import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientStatusBadge } from "../../../lib";

const meta = {
    title: "Business/Status/PatientStatusBadge",
    component: PatientStatusBadge,
    tags: ["autodocs"],
    args: {
        status: "active"
    }
} satisfies Meta<typeof PatientStatusBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Active: Story = {
    args: { status: "active" },
    parameters: {
        docs: {
            source: {
                code: `<PatientStatusBadge status="active" />`
            }
        }
    }
};

export const Inactive: Story = {
    args: { status: "inactive" },
    parameters: {
        docs: {
            source: {
                code: `<PatientStatusBadge status="inactive" />`
            }
        }
    }
};

export const Deceased: Story = {
    args: { status: "deceased" },
    parameters: {
        docs: {
            source: {
                code: `<PatientStatusBadge status="deceased" />`
            }
        }
    }
};

export const Archived: Story = {
    args: { status: "archived" },
    parameters: {
        docs: {
            source: {
                code: `<PatientStatusBadge status="archived" />`
            }
        }
    }
};
