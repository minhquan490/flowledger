package io.flowledger.platform.graphql.domain;

import java.util.List;
import java.util.Map;

/**
 * Request payload for a single read operation.
 *
 * @param model the logical model name handled by a read/search handler
 * @param key the primary key or unique key fields required to locate the record
 * @param fields optional field projections requested by the client
 */
public record GraphQlReadRequest(
    String model,
    Map<String, Object> key,
    List<String> fields
) {
}
