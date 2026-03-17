import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { textSnippet } from "../story-helpers";
import { Tabs } from "../../lib";

const meta = {
  title: "Components/Navigation/Tabs",
  component: Tabs,
  tags: ["autodocs"],
  args: {
    items: [
      { value: "overview", label: "Overview", content: textSnippet("Overview content") },
      { value: "details", label: "Details", content: textSnippet("Details content") },
      { value: "history", label: "History", content: textSnippet("History content") }
    ]
  },
  parameters: {
    docs: {
      description: {
        component: "Tab navigation wrapper composed from shadcn Tabs primitives."
      },
      source: {
        code: `<Tabs items={[{ value: "overview", label: "Overview" }]} />`
      }
    }
  }
} satisfies Meta<typeof Tabs>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
