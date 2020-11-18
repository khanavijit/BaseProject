package org.ak.project.domain;

import java.util.Arrays;

public enum DocsisVersion {
    V_3_0("3.0"), V_3_1("3.1");

    private final String version;

    DocsisVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static org.ak.project.domain.DocsisVersion fromVersion(final String version) throws org.ak.project.domain.UnknownDocsisVersionException {
        return Arrays.stream(values())
            .filter(value -> value.version.equals(version.replace("Docsis ", "")))
                .findFirst()
                .orElseThrow(() -> new org.ak.project.domain.UnknownDocsisVersionException(version));
    }
}
