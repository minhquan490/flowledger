import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Stat } from "../../lib";

const meta = {
  title: "Components/DataDisplay/Stat",
  component: Stat,
  tags: ["autodocs"],
  args: {
    label: "Total Patients",
    value: 1245,
    change: "+12% from last month",
    changeTone: "positive"
  }
} satisfies Meta<typeof Stat>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Stat label="Total Patients" value={1245} change="+12%" />` } } }
};
