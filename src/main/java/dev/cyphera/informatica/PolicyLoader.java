package dev.cyphera.informatica;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads policy definitions from a YAML file.
 *
 * Looks for the policy file at:
 *   1. System property "cyphera.policy.file"
 *   2. Environment variable "CYPHERA_POLICY_FILE"
 *   3. Default: /etc/cyphera/cyphera.yaml
 */
public final class PolicyLoader {

    private static final Logger LOG = Logger.getLogger(PolicyLoader.class.getName());
    private static volatile PolicyLoader instance;

    private final Map<String, PolicyEntry> policies = new HashMap<String, PolicyEntry>();

    private PolicyLoader() {
        String path = System.getProperty("cyphera.policy.file");
        if (path == null) {
            path = System.getenv("CYPHERA_POLICY_FILE");
        }
        if (path == null) {
            path = "/etc/cyphera/cyphera.yaml";
        }
        load(path);
    }

    public static PolicyLoader getInstance() {
        if (instance == null) {
            synchronized (PolicyLoader.class) {
                if (instance == null) {
                    instance = new PolicyLoader();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void load(String path) {
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(in);

            Map<String, Map<String, String>> keys;
            Object keysObj = root.get("keys");
            if (keysObj instanceof Map) {
                keys = (Map<String, Map<String, String>>) keysObj;
            } else {
                keys = Collections.emptyMap();
            }

            Map<String, Map<String, String>> pols;
            Object polsObj = root.get("policies");
            if (polsObj instanceof Map) {
                pols = (Map<String, Map<String, String>>) polsObj;
            } else {
                pols = Collections.emptyMap();
            }

            for (Map.Entry<String, Map<String, String>> entry : pols.entrySet()) {
                String name = entry.getKey();
                Map<String, String> def = entry.getValue();
                String engine = def.containsKey("engine") ? def.get("engine") : "ff1";
                String alphabet = def.containsKey("alphabet") ? def.get("alphabet") : "digits";
                String keyRef = def.containsKey("key_ref") ? def.get("key_ref") : "";
                String material = "";
                if (keys.containsKey(keyRef)) {
                    Map<String, String> keyDef = keys.get(keyRef);
                    material = keyDef.containsKey("material") ? keyDef.get("material") : "";
                }
                policies.put(name, new PolicyEntry(engine, alphabet, keyRef, material));
            }
            LOG.info("Loaded " + policies.size() + " policies from " + path);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not load policy file: " + path, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignored) {
                    // ignore close errors
                }
            }
        }
    }

    public PolicyEntry getPolicy(String name) {
        return policies.get(name);
    }
}
