package org.ak.project.logger.logger;

import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiagnosticContextScopeManager implements ScopeManager {

    private final ScopeManager scopeManager;
    private final SpanDiagnosticContext spanDiagnosticContext;

    public DiagnosticContextScopeManager(ScopeManager scopeManager, SpanDiagnosticContext spanDiagnosticContext) {
        this.scopeManager = Objects.requireNonNull(scopeManager);
        this.spanDiagnosticContext = Objects.requireNonNull(spanDiagnosticContext);
    }

    @Override
    public Scope activate(Span span) {
        // Activate scope
        Scope scope = scopeManager.activate(span);
        Map<String, String> context = spanDiagnosticContext.create(span);

        // Return wrapper
        return new DiagnosticContextScope(scope, context);
    }

    @Override
    public Span activeSpan() {
        return scopeManager.activeSpan();
    }

    /**
     * Wrapper class for {@link Scope}, which also closes attached {@link SpanDiagnosticContext}.
     * <p>
     * Created by {@link org.ak.project.logger.logger.DiagnosticContextScopeManager}.
     */
    public static class DiagnosticContextScope implements Scope {

        private final Scope scope;
        private final Map<String, String> previous = new HashMap<>();

        public DiagnosticContextScope(Scope scope, Map<String, String> context) {
            this.scope = scope;

            // Initialize MDC
            for (Map.Entry<String, String> entry : context.entrySet()) {
                this.previous.put(entry.getKey(), MDC.get(entry.getKey()));
                mdcReplace(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void close() {
            // Close
            scope.close();

            // Restore previous context
            for (Map.Entry<String, String> entry : previous.entrySet()) {
                mdcReplace(entry.getKey(), entry.getValue());
            }
        }

        private static void mdcReplace(String key, @Nullable String value) {
            if (value != null) {
                MDC.put(key, value);
            } else {
                MDC.remove(key);
            }
        }
    }
}
