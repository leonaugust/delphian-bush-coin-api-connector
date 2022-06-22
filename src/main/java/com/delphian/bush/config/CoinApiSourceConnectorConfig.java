package com.delphian.bush.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class CoinApiSourceConnectorConfig extends AbstractConfig {

    public static final String APPLICATION_CONFIG = "application";

    public static final String APPLICATION_DOC = "Will be used as a partition name";

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to write to";

    public static final String CRYPTO_PANIC_KEY_CONFIG = "crypto.panic.key";
    public static final String CRYPTO_PANIC_KEY_DOC = "Specify your crypto panic api key";


    public static final String PROFILE_ACTIVE_CONFIG = "profile.active";

    public static final String PROFILE_DOC = "Which profile is active";


    public static final String POLL_TIMEOUT_CONFIG = "poll.timeout";

    public static final String POLL_TIMEOUT_DOC = "How much time to wait between polls";

    public CoinApiSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public CoinApiSourceConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
                .define(APPLICATION_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, APPLICATION_DOC)
                .define(CRYPTO_PANIC_KEY_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, CRYPTO_PANIC_KEY_DOC)
                .define(PROFILE_ACTIVE_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, PROFILE_DOC)
                .define(POLL_TIMEOUT_CONFIG, ConfigDef.Type.LONG, ConfigDef.Importance.HIGH, POLL_TIMEOUT_DOC);
    }
}
