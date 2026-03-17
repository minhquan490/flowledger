import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { SimpleCard } from "../../lib";

const meta = {
    title: "Components/Layout/SimpleCard",
    component: SimpleCard,
    tags: ["autodocs"],
    args: {
        children: textSnippet("Simple card with padding and border")
    }
} satisfies Meta<typeof SimpleCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<SimpleCard>Simple card with padding and border</SimpleCard>`
            }
        }
    }
};
