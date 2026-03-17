import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { EmergencyContactForm } from "../../../lib";

const meta = {
    title: "Business/Contact/EmergencyContactForm",
    component: EmergencyContactForm,
    tags: ["autodocs"],
    args: {
        onSubmit: (values) => console.log("Form submitted:", values)
    }
} satisfies Meta<typeof EmergencyContactForm>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Empty: Story = {
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactForm \n  onSubmit={(values) => console.log(values)} \n/>`
            }
        }
    }
};

export const WithDefaultValues: Story = {
    args: {
        defaultValues: {
            name: "Jane Doe",
            relationship: "Spouse",
            phoneNumber: "5551234567"
        }
    },
    parameters: {
        docs: {
            source: {
                code: `<EmergencyContactForm \n  defaultValues={{ \n    name: "Jane Doe", \n    relationship: "Spouse", \n    phoneNumber: "5551234567" \n  }} \n  onSubmit={(values) => console.log(values)} \n/>`
            }
        }
    }
};
