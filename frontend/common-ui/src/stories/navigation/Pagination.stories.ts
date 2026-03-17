import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Pagination } from "../../lib";

const meta = {
  title: "Components/Navigation/Pagination",
  component: Pagination,
  tags: ["autodocs"],
  args: {
    count: 120,
    perPage: 10,
    page: 1,
    siblingCount: 1
  },
  parameters: {
    docs: {
      description: {
        component: "Pagination wrapper composed from shadcn Pagination primitives."
      },
      source: {
        code: `<Pagination count={120} perPage={10} page={1} />`
      }
    }
  }
} satisfies Meta<typeof Pagination>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
