import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { DataTable } from "../../lib";

const meta = {
  title: "Components/DataDisplay/DataTable",
  component: DataTable,
  tags: ["autodocs"],
  args: {
    columns: [
      { key: "name", label: "Name" },
      { key: "role", label: "Role" }
    ],
    rows: [
      { name: "John", role: "Admin" },
      { name: "Sarah", role: "Doctor" }
    ]
  }
} satisfies Meta<typeof DataTable>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<DataTable columns={[{ key: "name", label: "Name" }]} rows={[{ name: "John" }]} />` } } }
};
