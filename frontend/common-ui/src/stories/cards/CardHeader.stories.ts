import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { CardHeader } from "../../lib";

const meta = {
    title: "Components/Cards/CardHeader",
    component: CardHeader,
    tags: ["autodocs"],
    args: {
        children: textSnippet("Card Header Content")
    }
} satisfies Meta<typeof CardHeader>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<CardHeader>Card Header Content</CardHeader>`
            }
        }
    }
};
