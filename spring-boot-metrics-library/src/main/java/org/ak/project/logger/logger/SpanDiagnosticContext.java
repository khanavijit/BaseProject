package org.ak.project.logger.logger;

import io.opentracing.Span;

import java.util.Map;

@FunctionalInterface
public interface SpanDiagnosticContext {

    Map<String, String> create(Span span);
}