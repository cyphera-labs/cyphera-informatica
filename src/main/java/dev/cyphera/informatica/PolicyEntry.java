package dev.cyphera.informatica;

/**
 * Immutable policy definition loaded from cyphera.yaml.
 */
public final class PolicyEntry {
    private final String engine;
    private final String alphabet;
    private final String keyRef;
    private final String keyMaterial;

    public PolicyEntry(String engine, String alphabet, String keyRef, String keyMaterial) {
        this.engine = engine;
        this.alphabet = alphabet;
        this.keyRef = keyRef;
        this.keyMaterial = keyMaterial;
    }

    public String engine() { return engine; }
    public String alphabet() { return alphabet; }
    public String keyRef() { return keyRef; }
    public String keyMaterial() { return keyMaterial; }
}
