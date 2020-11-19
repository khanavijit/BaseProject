package org.ak.project.logger.logger;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.ScopeManager;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import io.opentracing.util.ThreadLocalScopeManager;
import org.springframework.lang.NonNull;

public class JaegerDiagnosticContextTracerBuilderCustomizer implements TracerBuilderCustomizer {

    private final ScopeManager scopeManager;
    private final SpanDiagnosticContext spanDiagnosticContext;

    public JaegerDiagnosticContextTracerBuilderCustomizer(@NonNull SpanDiagnosticContext spanDiagnosticContext) {
        scopeManager = new ThreadLocalScopeManager();
        this.spanDiagnosticContext = spanDiagnosticContext;
    }

    public JaegerDiagnosticContextTracerBuilderCustomizer(@NonNull ScopeManager scopeManager, @NonNull SpanDiagnosticContext spanDiagnosticContext) {
        this.scopeManager = scopeManager;
        this.spanDiagnosticContext = spanDiagnosticContext;
    }

    @Override
    public void customize(JaegerTracer.Builder builder) {
        builder.withScopeManager(new org.ak.project.logger.logger.DiagnosticContextScopeManager(scopeManager, spanDiagnosticContext));
    }
}
