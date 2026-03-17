import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import {Card, type CardProps} from "../../lib";

const meta = {
  title: "Components/Layout/Card",
  component: Card,
  tags: ["autodocs"],
  parameters: {
    docs: {
      description: {
        component: "Card container wrapper composed from shadcn Card primitive."
      }
    }
  },
  render: (args: CardProps) => ({
    Component: Card,
    props: args
  }),
  args: {
    children: textSnippet("Card content")
  }
} satisfies Meta<typeof Card>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<Card>Card content</Card>`
      }
    }
  }
};
