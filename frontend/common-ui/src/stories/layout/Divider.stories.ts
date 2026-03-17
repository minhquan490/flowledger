import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { Divider } from "../../lib";

interface DividerStoryArgs {
  orientation?: "horizontal" | "vertical";
  decorative?: boolean;
}

const meta = {
  title: "Components/Layout/Divider",
  component: Divider,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Visual separator wrapper composed from shadcn Separator primitive."
      }
    }
  },
  render: (args) => ({
    Component: Divider,
    props: args
  }),
  args: {
    orientation: "horizontal",
    decorative: true
  },
  argTypes: {
    orientation: {
      control: "select",
      options: ["horizontal", "vertical"]
    },
    decorative: {
      control: "boolean"
    }
  }
} satisfies Meta<DividerStoryArgs>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Horizontal: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Divider orientation="horizontal" />`
      }
    }
  }
};

export const Vertical: Story = {
  args: {
    orientation: "vertical"
  },
  parameters: {
    docs: {
      source: {
        code: `<Divider orientation="vertical" />`
      }
    }
  }
};
