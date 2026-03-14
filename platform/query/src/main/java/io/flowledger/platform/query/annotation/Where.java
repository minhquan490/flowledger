package io.flowledger.platform.query.annotation;

import io.flowledger.platform.query.mapping.AnnotationValue;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a {@code WHERE} clause fragment for a mapped query.
 *
 * <p>Can be placed on a mapper type to apply to all methods or on a specific
 * mapping method to apply only to that query.
 *
 * <p>Example:
 * <pre>{@code
 * public interface LedgerView extends BlazeView<LedgerRow> {
 *   @Where("status = 'POSTED'")
 *   List<LedgerRow> postedRows();
 * }
 * }</pre>
 *
 * <p>Type-level example:
 * <pre>{@code
 * @Where("tenant_id = :tenantId")
 * public interface TenantLedgerView extends BlazeView<LedgerRow> {
 *   List<LedgerRow> allRows();
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AnnotationValue
public @interface Where {
  @AliasFor(annotation = AnnotationValue.class, attribute = AnnotationValue.VALUE_METHOD_NAME)
  String value();
}
