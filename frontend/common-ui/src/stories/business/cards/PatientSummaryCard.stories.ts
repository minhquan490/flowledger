import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientSummaryCard } from "../../../lib";

const meta = {
    title: "Business/Cards/PatientSummaryCard",
    component: PatientSummaryCard,
    tags: ["autodocs"],
    args: {
        firstName: "John",
        lastName: "Doe",
        dateOfBirth: "1985-06-15",
        gender: "male",
        status: "active",
        identifier: "MRN-778899"
    }
} satisfies Meta<typeof PatientSummaryCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<PatientSummaryCard 
  firstName="John" 
  lastName="Doe" 
  dateOfBirth="1985-06-15" 
  gender="male" 
  status="active" 
  identifier="MRN-778899" 
/>`
            }
        }
    }
};

export const WithAvatar: Story = {
    args: {
        avatarSrc: "https://github.com/shadcn.png"
    },
    parameters: {
        docs: {
            source: {
                code: `<PatientSummaryCard 
  firstName="John" 
  lastName="Doe" 
  dateOfBirth="1985-06-15" 
  gender="male" 
  status="active" 
  identifier="MRN-778899"
  avatarSrc="https://github.com/shadcn.png"
/>`
            }
        }
    }
};
