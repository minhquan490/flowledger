import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { RecordMetaPanel } from "../../../lib";

const meta = {
    title: "Components/Business/Shared/RecordMetaPanel",
    component: RecordMetaPanel,
    tags: ["autodocs"],
    parameters: {
        docs: {
            description: {
                component: "A small panel for audit metadata (Created by, Updated by)."
            }
        }
    }
} satisfies Meta<typeof RecordMetaPanel>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        createdBy: "Dr. Alice Johnson",
        createdAt: "2023-10-27T10:00:00Z",
        updatedBy: "Nurse Bob Wilson",
        updatedAt: "2023-10-28T14:30:00Z"
    },
    parameters: {
        docs: {
            source: {
                code: `<RecordMetaPanel 
  createdBy="Dr. Alice Johnson" 
  createdAt="2023-10-27T10:00:00Z" 
  updatedBy="Nurse Bob Wilson" 
  updatedAt="2023-10-28T14:30:00Z" 
/>`
            }
        }
    }
};
