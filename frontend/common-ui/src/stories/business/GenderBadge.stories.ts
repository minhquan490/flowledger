import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { GenderBadge } from "../../lib";

const meta = {
    title: "Business/GenderBadge",
    component: GenderBadge,
    tags: ["autodocs"],
    args: {
        gender: "male"
    }
} satisfies Meta<typeof GenderBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Male: Story = {
    args: {
        gender: "male"
    },
    parameters: {
        docs: {
            source: {
                code: `<GenderBadge gender="male" />`
            }
        }
    }
};

export const Female: Story = {
    args: {
        gender: "female"
    },
    parameters: {
        docs: {
            source: {
                code: `<GenderBadge gender="female" />`
            }
        }
    }
};

export const Other: Story = {
    args: {
        gender: "other"
    },
    parameters: {
        docs: {
            source: {
                code: `<GenderBadge gender="other" />`
            }
        }
    }
};

export const Unknown: Story = {
    args: {
        gender: "unknown"
    },
    parameters: {
        docs: {
            source: {
                code: `<GenderBadge gender="unknown" />`
            }
        }
    }
};
