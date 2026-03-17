import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PatientPicker } from "../../../lib";

const mockPatients = [
    { id: "1", name: "John Doe", identifier: "MRN-123" },
    { id: "2", name: "Jane Smith", identifier: "MRN-456" },
    { id: "3", name: "Bob Wilson", identifier: "MRN-789" }
];

const meta = {
    title: "Business/Search/PatientPicker",
    component: PatientPicker,
    tags: ["autodocs"],
    args: {
        patients: mockPatients,
        placeholder: "Select a patient..."
    }
} satisfies Meta<typeof PatientPicker>;

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

<PatientPicker
  {patients}
  placeholder="Select a patient..."
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
                code: `<PatientPicker patients={[]} loading={true} />`
            }
        }
    }
};

export const Selected: Story = {
    args: {
        selectedPatient: mockPatients[0]
    },
    parameters: {
        docs: {
            source: {
                code: `<script lang="ts">
  const patients = [
    { id: "1", name: "John Doe", identifier: "MRN-123" },
    { id: "2", name: "Jane Smith", identifier: "MRN-456" }
  ];
</script>

<PatientPicker
  {patients}
  selectedPatient={patients[0]}
/>`
            }
        }
    }
};
