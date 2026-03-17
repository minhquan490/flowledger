import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientAvatar } from "../../lib";

const meta = {
    title: "Business/PatientAvatar",
    component: PatientAvatar,
    tags: ["autodocs"],
    args: {
        firstName: "John",
        lastName: "Doe"
    }
} satisfies Meta<typeof PatientAvatar>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<PatientAvatar firstName="John" lastName="Doe" />`
            }
        }
    }
};

export const WithImage: Story = {
    args: {
        src: "https://github.com/shadcn.png"
    },
    parameters: {
        docs: {
            source: {
                code: `<PatientAvatar
  firstName="John"
  lastName="Doe"
  src="https://github.com/shadcn.png"
/>`
            }
        }
    }
};
