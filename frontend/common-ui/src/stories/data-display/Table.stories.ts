import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Table } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Table",
  component: Table,
  tags: ["autodocs"],
  args: {
    headers: ["Name", "Age", "Status"],
    rows: [
      ["John", 29, "Active"],
      ["Sarah", 34, "Inactive"]
    ]
  }
} satisfies Meta<typeof Table>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Table headers={["Name", "Age"]} rows={[["John", 29]]} />` } } }
};
