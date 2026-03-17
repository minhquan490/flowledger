import type {AnyFieldApi} from "@tanstack/form-core";
import type {ZodType} from "zod";

import type {TanStackValidatorFn} from "./types.js";

export interface ZodFieldValidatorOptions {
  /**
   * When provided, overrides Zod's first issue message.
   */
  message?: string;
}

/**
 * Adapts a Zod schema into a TanStack field validator.
 *
 * Usage:
 * `validators={{ onChange: zodFieldValidator(z.string().min(1, "Required")) }}`
 */
export const zodFieldValidator = (
  schema: ZodType<unknown>,
  opts?: ZodFieldValidatorOptions
): TanStackValidatorFn => {
  return ({value, fieldApi}: {value: unknown; fieldApi: AnyFieldApi}) => {
    const res = schema.safeParse(value);
    if (res.success) return undefined;

    if (opts?.message) return opts.message;

    const first = res.error.issues[0];
    return first?.message || `${fieldApi.name} is invalid`;
  };
};

