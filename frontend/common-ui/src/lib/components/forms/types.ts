import type {
  AnyFieldApi,
  DeepKeys,
  FormApi,
  FormAsyncValidateOrFn,
  FormValidateOrFn
} from "@tanstack/form-core";
import type { SvelteFormApi } from "@tanstack/svelte-form";
import type { DateValue } from "@internationalized/date";
import type { ComponentProps, Snippet } from "svelte";
import { createForm } from "@tanstack/svelte-form";

export interface CheckboxProps {
  checked?: boolean;
  indeterminate?: boolean;
  disabled?: boolean;
  label?: string;
  description?: string;
  class?: string;
  id?: string;
}

export interface ComboboxOption {
  value: string;
  label: string;
  keywords?: string[];
  disabled?: boolean;
}

export interface ComboboxProps {
  value?: string;
  options?: ComboboxOption[];
  placeholder?: string;
  searchPlaceholder?: string;
  emptyText?: string;
  class?: string;
  contentClass?: string;
  disabled?: boolean;
}

export interface DatePickerProps {
  value?: DateValue;
  placeholder?: string;
  class?: string;
  disabled?: boolean;
}

export interface DateRangePickerProps {
  start?: DateValue;
  end?: DateValue;
  class?: string;
  startPlaceholder?: string;
  endPlaceholder?: string;
}

export interface ErrorTextProps {
  class?: string;
  children?: Snippet;
}

export interface FileUploadProps {
  files?: FileList;
  accept?: string;
  multiple?: boolean;
  disabled?: boolean;
  class?: string;
  helperText?: string;
  id?: string;
}

export interface FormFieldProps {
  id?: string;
  label?: string;
  required?: boolean;
  helperText?: string;
  errorText?: string;
  class?: string;
  children?: Snippet;
}

export interface HelperTextProps {
  class?: string;
  children?: Snippet;
}



export type LabelProps = ComponentProps<
  typeof import("../ui/label/index.js").Label
> & {
  required?: boolean;
};

export type InputProps = ComponentProps<typeof import("../ui/input/index.js").Input>;
export type NonFileInputProps = Omit<InputProps, "files">;
export type FileInputProps = Omit<InputProps, "value">;

export interface PhoneInputProps {
  value?: string;
  placeholder?: string;
  class?: string;
  disabled?: boolean;
  id?: string;
}

export interface RadioOption {
  value: string;
  label: string;
  disabled?: boolean;
}

export interface RadioGroupProps {
  value?: string;
  options?: RadioOption[];
  class?: string;
  disabled?: boolean;
  orientation?: "vertical" | "horizontal";
}

export interface RequiredMarkProps {
  class?: string;
}

export interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
}

export interface SelectProps {
  value?: string;
  options?: SelectOption[];
  placeholder?: string;
  class?: string;
  triggerClass?: string;
  contentClass?: string;
  disabled?: boolean;
}

export interface SwitchProps {
  checked?: boolean;
  disabled?: boolean;
  label?: string;
  description?: string;
  class?: string;
  id?: string;
}

export type TextareaProps = ComponentProps<
  typeof import("../ui/textarea/index.js").Textarea
> & {
  value?: string;
};

// TanStack form helpers (used by TanStackFormField.svelte)
export type TanStackValidatorFn = (props: { value: unknown; fieldApi: AnyFieldApi }) => unknown;
export type TanStackValidatorAsyncFn = (props: {
  value: unknown;
  fieldApi: AnyFieldApi;
  signal: AbortSignal;
}) => unknown | Promise<unknown>;

export type TanStackFieldValidators = {
  onMount?: TanStackValidatorFn;
  onChange?: TanStackValidatorFn;
  onBlur?: TanStackValidatorFn;
  onSubmit?: TanStackValidatorFn;
  onDynamic?: TanStackValidatorFn;
  onChangeAsync?: TanStackValidatorAsyncFn;
  onBlurAsync?: TanStackValidatorAsyncFn;
  onSubmitAsync?: TanStackValidatorAsyncFn;
  onDynamicAsync?: TanStackValidatorAsyncFn;
};

export type TanStackShowError = "touched" | "always" | "never";

export type TanStackSvelteFormApi<
  TData,
  TFormOnMount extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChange extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChangeAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnBlur extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnBlurAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnSubmit extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnSubmitAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnDynamic extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnDynamicAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnServer extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TSubmitMeta = never
> = SvelteFormApi<
  TData,
  TFormOnMount,
  TFormOnChange,
  TFormOnChangeAsync,
  TFormOnBlur,
  TFormOnBlurAsync,
  TFormOnSubmit,
  TFormOnSubmitAsync,
  TFormOnDynamic,
  TFormOnDynamicAsync,
  TFormOnServer,
  TSubmitMeta
>;

export type TanStackFieldName<TData> = DeepKeys<TData>;

export type TanStackTypedFormApi<
  TData,
  TFormOnMount extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChange extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnChangeAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnBlur extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnBlurAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnSubmit extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnSubmitAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnDynamic extends undefined | FormValidateOrFn<TData> = undefined,
  TFormOnDynamicAsync extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TFormOnServer extends undefined | FormAsyncValidateOrFn<TData> = undefined,
  TSubmitMeta = never
> = FormApi<
  TData,
  TFormOnMount,
  TFormOnChange,
  TFormOnChangeAsync,
  TFormOnBlur,
  TFormOnBlurAsync,
  TFormOnSubmit,
  TFormOnSubmitAsync,
  TFormOnDynamic,
  TFormOnDynamicAsync,
  TFormOnServer,
  TSubmitMeta
> &
  TanStackSvelteFormApi<
    TData,
    TFormOnMount,
    TFormOnChange,
    TFormOnChangeAsync,
    TFormOnBlur,
    TFormOnBlurAsync,
    TFormOnSubmit,
    TFormOnSubmitAsync,
    TFormOnDynamic,
    TFormOnDynamicAsync,
    TFormOnServer,
    TSubmitMeta
  >;

// Convenience type for non-generic component props (when you don't care about TData)
export type AnyTanStackFormApi = TanStackTypedFormApi<
// eslint-disable-next-line @typescript-eslint/no-explicit-any
  any,
  FormValidateOrFn<any> | undefined,
  FormValidateOrFn<any> | undefined,
  FormAsyncValidateOrFn<any> | undefined,
  FormValidateOrFn<any> | undefined,
  FormAsyncValidateOrFn<any> | undefined,
  FormValidateOrFn<any> | undefined,
  FormAsyncValidateOrFn<any> | undefined,
  FormValidateOrFn<any> | undefined,
  FormAsyncValidateOrFn<any> | undefined,
  FormAsyncValidateOrFn<any> | undefined,
  any
>;

export type OnInputEvent = Event & { currentTarget: (EventTarget & HTMLInputElement) };

export type OnTextareaEvent = Event & { currentTarget: (EventTarget & HTMLTextAreaElement) };

export type HtmlFocusEvent = FocusEvent & { currentTarget: (EventTarget & HTMLInputElement) }

export type FormType = ReturnType<typeof createForm>
