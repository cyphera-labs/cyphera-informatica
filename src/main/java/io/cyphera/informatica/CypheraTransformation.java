package io.cyphera.informatica;

import io.cyphera.Cyphera;

/**
 * Entry point for Informatica Java transformations.
 *
 * In PowerCenter or IDMC, add a Java Transformation and call these static
 * methods to encrypt/decrypt fields inline during ETL.
 */
public final class CypheraTransformation {

    private CypheraTransformation() {}

    private static final Cyphera CLIENT = CypheraLoader.getInstance();

    public static String cyphera_protect(String policyName, String value) {
        try { return CLIENT.protect(value, policyName); }
        catch (Exception e) { return "[error: " + e.getMessage() + "]"; }
    }

    public static String cyphera_unprotect(String protectedValue) {
        try { return CLIENT.access(protectedValue); }
        catch (Exception e) { return "[error: " + e.getMessage() + "]"; }
    }

    public static String cyphera_access(String policyName, String protectedValue) {
        try { return CLIENT.access(protectedValue, policyName); }
        catch (Exception e) { return "[error: " + e.getMessage() + "]"; }
    }
}
