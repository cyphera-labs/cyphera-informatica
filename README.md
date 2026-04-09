# cyphera-informatica

Cyphera format-preserving encryption for Informatica PowerCenter and IDMC.

Deployed as a custom Java transformation (mapplet) — encrypts and decrypts fields inline during ETL.

## Build

```bash
mvn package -DskipTests
```

Produces `target/cyphera-informatica-0.1.0.jar` (fat JAR, Java 8 compatible).

## Deploy to PowerCenter

1. Copy `cyphera-informatica-0.1.0.jar` to the PowerCenter Java transformation classpath
2. In the mapping, add a Java Transformation
3. Call `io.cyphera.informatica.CypheraTransformation.cyphera_protect("ssn", inputField)` in the transform logic

## Deploy to IDMC (Informatica Cloud)

1. Upload `cyphera-informatica-0.1.0.jar` as a custom library
2. Reference in a Java transformation within your mapping

## Usage

In a Java Transformation expression:
```java
// Protect with a named policy
String protectedValue = io.cyphera.informatica.CypheraTransformation.cyphera_protect("ssn", input_ssn);
// → "T01948372150" (tagged, format preserved)

// Access — tag tells Cyphera which policy to use
String accessed = io.cyphera.informatica.CypheraTransformation.cyphera_access(protectedValue);
// → original SSN
```

## Policy Configuration

Place `cyphera.json` at `/etc/cyphera/cyphera.json` or set the `CYPHERA_POLICY_FILE` environment variable.
