import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Spinner } from "../../lib";

const meta = {
  title: "Components/Feedback/Spinner",
  component: Spinner,
  tags: ["autodocs"],
  args: {
    size: "md"
  }
} satisfies Meta<typeof Spinner>;

export default meta;
export const Default: StoryObj<typeof meta> = {
  parameters: { docs: { source: { code: `<Spinner size=\"md\" />` } } }
};
