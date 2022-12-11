package com.delphian.bush.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.Arrays;
import java.util.List;

public class ActiveProfileValidator implements ConfigDef.Validator{

    public static final String TEST_PROFILE = "test";
    public static final String PROD_PROFILE = "prod";

    List<String> availableProfiles = Arrays.asList(TEST_PROFILE, PROD_PROFILE);

    @Override
    public void ensureValid(String name, Object value) {
        String profile = (String) value;
        if (!availableProfiles.contains(profile)) {
            throw new ConfigException(name, value, "Selected profile is invalid, available profiles "  + availableProfiles);
        }

    }
}
