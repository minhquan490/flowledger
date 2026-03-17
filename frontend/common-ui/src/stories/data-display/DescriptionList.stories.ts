import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { DescriptionList } from "../../lib";

const meta = {
  title: "Components/DataDisplay/DescriptionList",
  component: DescriptionList,
  tags: ["autodocs"],
  args: {
    items: [
      { term: "Patient ID", description: "PT-001" },
      { term: "Blood Type", description: "O+" }
    ]
  }
} satisfies Meta<typeof DescriptionList>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<DescriptionList items={[{ term: "ID", description: "PT-001" }]} />` } } }
};
