package io.flowledger.platform.graphql.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares the GraphQL model name for a Blaze entity view.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface GraphQlModel {

  /**
   * Returns the model name used in GraphQL requests.
   *
   * @return the model name
   */
  String value();
}
