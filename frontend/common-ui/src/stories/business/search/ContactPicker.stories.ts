import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { ContactPicker } from "../../../lib";

const mockContacts = [
    { id: "1", name: "Jane Doe", relationship: "Spouse" },
    { id: "2", name: "James Smith", relationship: "Parent" },
    { id: "3", name: "Robert Johnson", relationship: "Brother" }
];

const meta = {
    title: "Business/Search/ContactPicker",
    component: ContactPicker,
    tags: ["autodocs"],
    args: {
        contacts: mockContacts,
        placeholder: "Select an emergency contact..."
    }
} satisfies Meta<typeof ContactPicker>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<script lang="ts">
  const contacts = [
    { id: "1", name: "Jane Doe", relationship: "Spouse" },
    { id: "2", name: "James Smith", relationship: "Parent" },
    { id: "3", name: "Robert Johnson", relationship: "Brother" }
  ];
</script>

<ContactPicker
  {contacts}
  placeholder="Select an emergency contact..."
/>`
            }
        }
    }
};

export const Loading: Story = {
    args: {
        loading: true
    },
    parameters: {
        docs: {
            source: {
                code: `<ContactPicker contacts={[]} loading={true} />`
            }
        }
    }
};

export const Selected: Story = {
    args: {
        selectedContact: mockContacts[0]
    },
    parameters: {
        docs: {
            source: {
                code: `<script lang="ts">
  const contacts = [
    { id: "1", name: "Jane Doe", relationship: "Spouse" },
    { id: "2", name: "James Smith", relationship: "Parent" }
  ];
</script>

<ContactPicker
  {contacts}
  selectedContact={contacts[0]}
/>`
            }
        }
    }
};
