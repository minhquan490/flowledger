import type { Meta, StoryObj } from "@storybook/svelte-vite";
import { PersonName } from "../../lib";

const meta = {
    title: "Business/PersonName",
    component: PersonName,
    tags: ["autodocs"],
    args: {
        firstName: "John",
        lastName: "Doe",
        format: "lastFirst"
    }
} satisfies Meta<typeof PersonName>;

export default meta;
type Story = StoryObj<typeof meta>;

export const LastFirst: Story = {
    parameters: {
        docs: {
            source: {
                code: `<PersonName firstName="John" lastName="Doe" format="lastFirst" />`
            }
        }
    }
};

export const FirstLast: Story = {
    args: {
        format: "firstLast"
    },
    parameters: {
        docs: {
            source: {
                code: `<PersonName
  firstName="John"
  lastName="Doe"
  format="firstLast"
/>`
            }
        }
    }
};

export const Short: Story = {
    args: {
        format: "short"
    },
    parameters: {
        docs: {
            source: {
                code: `<PersonName
  firstName="John"
  lastName="Doe"
  format="short"
/>`
            }
        }
    }
};

export const FullName: Story = {
    args: {
        prefix: "Mr.",
        firstName: "John",
        middleName: "Quincy",
        lastName: "Doe",
        suffix: "Jr.",
        format: "firstLast"
    },
    parameters: {
        docs: {
            source: {
                code: `<PersonName
  prefix="Mr."
  firstName="John"
  middleName="Quincy"
  lastName="Doe"
  suffix="Jr."
  format="firstLast"
/>`
            }
        }
    }
};
