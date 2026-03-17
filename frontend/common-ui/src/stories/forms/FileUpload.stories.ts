import type { Meta, StoryObj } from "@storybook/svelte-vite";
import {FileUploadExample} from "./example";

const meta = {
  title: "Components/Forms/FileUpload",
  component: FileUploadExample,
  tags: ["autodocs"],
  args: {
    label: "Attachments",
    helperText: "PDF/JPG up to 10MB",
    multiple: true
  }
} satisfies Meta<typeof FileUploadExample>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {
  parameters: {
    docs: {
      source: {
        code: `<FileUpload form={form} name="files" label="Attachments" helperText="PDF/JPG up to 10MB" multiple={true} />`
      }
    }
  }
};
