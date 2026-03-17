import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { CardContent } from "../../lib";

const meta = {
    title: "Components/Cards/CardContent",
    component: CardContent,
    tags: ["autodocs"],
    args: {
        children: textSnippet("Card Content Area")
    }
} satisfies Meta<typeof CardContent>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<CardContent>Card Content Area</CardContent>`
            }
        }
    }
};
