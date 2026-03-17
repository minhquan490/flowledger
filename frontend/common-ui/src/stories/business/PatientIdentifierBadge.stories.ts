import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientIdentifierBadge } from "../../lib";

const meta = {
    title: "Business/PatientIdentifierBadge",
    component: PatientIdentifierBadge,
    tags: ["autodocs"],
    args: {
        identifier: "MRN-123456",
        type: "MRN",
        copyable: true
    }
} satisfies Meta<typeof PatientIdentifierBadge>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<PatientIdentifierBadge identifier="MRN-123456" type="MRN" copyable={true} />`
            }
        }
    }
};

export const SSN: Story = {
    args: {
        identifier: "XXX-XX-XXXX",
        type: "SSN",
        copyable: false
    },
    parameters: {
        docs: {
            source: {
                code: `<PatientIdentifierBadge
  identifier="XXX-XX-XXXX"
  type="SSN"
  copyable={false}
/>`
            }
        }
    }
};
