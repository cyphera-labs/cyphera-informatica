package dev.cyphera.informatica;

/**
 * Entry point for Informatica Java transformations.
 *
 * In PowerCenter or IDMC, add a Java Transformation and call these static
 * methods to encrypt/decrypt fields inline during ETL.
 */
public final class CypheraTransformation {

    private CypheraTransformation() {}

    /**
     * Encrypt a value using a named policy from cyphera.yaml.
     * Use as: cyphera_protect("ssn", inputValue)
     */
    public static String cyphera_protect(String policyName, String value) {
        PolicyEntry policy = PolicyLoader.getInstance().getPolicy(policyName);
        if (policy == null) {
            return "[unknown policy: " + policyName + "]";
        }
        return DummyCipher.encrypt(value, policy.alphabet(), policy.keyMaterial());
    }

    /**
     * Decrypt a value using a named policy from cyphera.yaml.
     */
    public static String cyphera_unprotect(String policyName, String value) {
        PolicyEntry policy = PolicyLoader.getInstance().getPolicy(policyName);
        if (policy == null) {
            return "[unknown policy: " + policyName + "]";
        }
        return DummyCipher.decrypt(value, policy.alphabet(), policy.keyMaterial());
    }

    /**
     * Direct FF1 encrypt (for testing).
     */
    public static String cyphera_ff1_encrypt(String value, String keyHex, String alphabet) {
        return DummyCipher.encrypt(value, alphabet, keyHex);
    }

    /**
     * Direct FF1 decrypt (for testing).
     */
    public static String cyphera_ff1_decrypt(String value, String keyHex, String alphabet) {
        return DummyCipher.decrypt(value, alphabet, keyHex);
    }
}
