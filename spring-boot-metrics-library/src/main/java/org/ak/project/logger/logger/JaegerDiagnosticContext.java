package org.ak.project.logger.logger;


import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Span;

import java.util.HashMap;
import java.util.Map;

public class JaegerDiagnosticContext implements org.ak.project.logger.logger.SpanDiagnosticContext {

    @Override
    public Map<String, String> create(Span span) {
        // Get Jaeger context
        JaegerSpanContext spanContext = (JaegerSpanContext) span.context();

        // Prepare context map
        Map<String, String> contextMap = new HashMap<>(3);
        contextMap.put("traceId", spanContext.getTraceId());
        contextMap.put("spanId", Long.toHexString(spanContext.getSpanId()));
        contextMap.put("flags", Integer.toHexString(spanContext.getFlags()));

        return contextMap;
    }
}
