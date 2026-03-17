import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { AgeDisplay } from "../../lib";

const meta = {
    title: "Business/AgeDisplay",
    component: AgeDisplay,
    tags: ["autodocs"],
    args: {
        dateOfBirth: "1990-01-01",
        format: "full"
    }
} satisfies Meta<typeof AgeDisplay>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<AgeDisplay dateOfBirth="1990-01-01" format="full" />`
            }
        }
    }
};

export const Short: Story = {
    args: {
        format: "short"
    }
};

export const Infant: Story = {
    args: {
        dateOfBirth: new Date(new Date().setMonth(new Date().getMonth() - 5)).toISOString()
    }
};

export const Deceased: Story = {
    args: {
        dateOfBirth: "1950-01-01",
        deceasedDate: "2020-01-01"
    }
};
