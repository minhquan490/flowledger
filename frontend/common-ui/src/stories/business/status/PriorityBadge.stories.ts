import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PriorityBadge } from "../../../lib";

const meta = {
    title: "Business/Status/PriorityBadge",
    component: PriorityBadge,
    tags: ["autodocs"],
    args: {
        priority: "medium"
    }
} satisfies Meta<typeof PriorityBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Low: Story = {
    args: { priority: "low" },
    parameters: {
        docs: {
            source: {
                code: `<PriorityBadge priority="low" />`
            }
        }
    }
};

export const Medium: Story = {
    args: { priority: "medium" },
    parameters: {
        docs: {
            source: {
                code: `<PriorityBadge priority="medium" />`
            }
        }
    }
};

export const High: Story = {
    args: { priority: "high" },
    parameters: {
        docs: {
            source: {
                code: `<PriorityBadge priority="high" />`
            }
        }
    }
};

export const Urgent: Story = {
    args: { priority: "urgent" },
    parameters: {
        docs: {
            source: {
                code: `<PriorityBadge priority="urgent" />`
            }
        }
    }
};
