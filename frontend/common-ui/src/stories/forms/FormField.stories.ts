import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { FormField } from '../../lib';

const meta = {
    title: "Components/Forms/FormField",
    component: FormField,
    tags: ["autodocs"],
    args: {
        label: "Username",
        id: "username",
        children: textSnippet('<input class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50" id="username" placeholder="Enter username" />')
    }
} satisfies Meta<typeof FormField>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<FormField label="Username" id="username">\n  <input class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50" id="username" placeholder="Enter username" />\n</FormField>`
            }
        }
    }
};

export const WithHelperText: Story = {
    args: {
        helperText: "Choose a unique username."
    }
};

export const WithError: Story = {
    args: {
        errorText: "Username is already taken."
    }
};

export const Required: Story = {
    args: {
        required: true
    }
};
