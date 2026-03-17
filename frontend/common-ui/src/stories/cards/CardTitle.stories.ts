import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { CardTitle } from "../../lib";

const meta = {
    title: "Components/Cards/CardTitle",
    component: CardTitle,
    tags: ["autodocs"],
    args: {
        children: textSnippet("Card Title")
    }
} satisfies Meta<typeof CardTitle>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<CardTitle>Card Title</CardTitle>`
            }
        }
    }
};
