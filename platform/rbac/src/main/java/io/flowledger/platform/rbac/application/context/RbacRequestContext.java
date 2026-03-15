package io.flowledger.platform.rbac.application.context;

import io.flowledger.platform.rbac.domain.RbacAction;

/**
 * Captures RBAC request context for query customization.
 *
 * @param resource the resource name
 * @param action the action being evaluated
 */
public record RbacRequestContext(String resource, RbacAction action) {
}
