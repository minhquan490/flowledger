import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientSearchBox } from "../../../lib";

const mockPatients = [
    { id: "1", name: "John Doe", identifier: "MRN-123" },
    { id: "2", name: "Jane Smith", identifier: "MRN-456" },
    { id: "3", name: "Bob Wilson", identifier: "MRN-789" }
];

const meta = {
    title: "Business/Search/PatientSearchBox",
    component: PatientSearchBox,
    tags: ["autodocs"],
    args: {
        patients: mockPatients,
        placeholder: "Search for a patient by name or MRN..."
    }
} satisfies Meta<typeof PatientSearchBox>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    parameters: {
        docs: {
            source: {
                code: `<script lang="ts">
  const patients = [
    { id: "1", name: "John Doe", identifier: "MRN-123" },
    { id: "2", name: "Jane Smith", identifier: "MRN-456" },
    { id: "3", name: "Bob Wilson", identifier: "MRN-789" }
  ];
</script>

<PatientSearchBox
  {patients}
  placeholder="Search for a patient by name or MRN..."
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
                code: `<PatientSearchBox patients={[]} loading={true} />`
            }
        }
    }
};

export const NoResults: Story = {
    args: {
        patients: []
    },
    parameters: {
        docs: {
            source: {
                code: `<PatientSearchBox
  patients={[]}
  placeholder="No patients found matching your search."
/>`
            }
        }
    }
};
