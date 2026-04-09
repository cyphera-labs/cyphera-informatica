# cyphera-informatica

[![CI](https://github.com/cyphera-labs/cyphera-informatica/actions/workflows/ci.yml/badge.svg)](https://github.com/cyphera-labs/cyphera-informatica/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](LICENSE)

Format-preserving encryption for [Informatica](https://www.informatica.com/) — Java transformation powered by Cyphera.

Built on [`io.cyphera:cyphera`](https://central.sonatype.com/artifact/io.cyphera/cyphera) from Maven Central. Java 8 compatible.

> This integration requires Informatica PowerCenter or IDMC. See below for deployment instructions.

## Build

### From source

```bash
mvn package -DskipTests
```

Produces `target/cyphera-informatica-0.1.0.jar` (fat JAR, Java 8 compatible).

### Via Docker

```bash
docker build -t cyphera-informatica .
```

## Install / Deploy

### PowerCenter

1. Copy `cyphera-informatica-0.1.0.jar` to the PowerCenter Java transformation classpath
2. In the mapping, add a Java Transformation
3. Call the Cyphera methods in the transform logic (see Usage below)

### IDMC (Informatica Cloud)

1. Upload `cyphera-informatica-0.1.0.jar` as a custom library
2. Reference in a Java transformation within your mapping

### Policy Configuration

Place `cyphera.json` at `/etc/cyphera/cyphera.json` or set the `CYPHERA_POLICY_FILE` environment variable.

## Usage

In a Java Transformation expression:

```java
// Protect with a named policy
String protectedValue = io.cyphera.informatica.CypheraTransformation.cyphera_protect("ssn", input_ssn);
// → "T01i6J-xF-07pX" (tagged, dashes preserved)

// Access — tag tells Cyphera which policy to use
String accessed = io.cyphera.informatica.CypheraTransformation.cyphera_access(protectedValue);
// → original SSN
```

### Available Methods

| Method | Description |
|--------|-------------|
| `cyphera_protect(policy, value)` | Protect using a named policy |
| `cyphera_access(protectedValue)` | Access using tag-based policy lookup |
| `cyphera_access(policy, protectedValue)` | Access with explicit policy name |

## Operations

### Policy Configuration

- Policy file: `/etc/cyphera/cyphera.json` or `CYPHERA_POLICY_FILE` env var
- For PowerCenter: set env var on the Integration Service
- For IDMC: set env var in the Secure Agent configuration
- Policy loaded on first call — restart the service to reload

### Monitoring

- Errors return `[error: message]` as the output value
- Check PowerCenter session logs or IDMC job logs for details

### Upgrading

1. Build a new JAR with the updated SDK version
2. Replace the JAR on the classpath (PowerCenter) or re-upload (IDMC)
3. Restart the Integration Service / Secure Agent

### Troubleshooting

- **ClassNotFoundException** — JAR not on the classpath. Verify placement and restart.
- **"Unknown policy"** — policy file not found or policy name misspelled
- **Java version mismatch** — this JAR requires Java 8+. Check your Informatica JRE version.

## Policy File

```json
{
  "policies": {
    "ssn": { "engine": "ff1", "key_ref": "demo-key", "tag": "T01" },
    "credit_card": { "engine": "ff1", "key_ref": "demo-key", "tag": "T02" }
  },
  "keys": {
    "demo-key": { "material": "2B7E151628AED2A6ABF7158809CF4F3C" }
  }
}
```

## Future

- Informatica Marketplace listing
- Pre-built mapplet templates for common PII types (SSN, credit card, phone)
- IDMC native connector (beyond Java transformation)

## License

Apache 2.0 — Copyright 2026 Horizon Digital Engineering LLC
