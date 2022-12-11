package com.delphian.bush.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

public class PollTimeoutValidator implements ConfigDef.Validator{

    public static final long MIN_POLL = 10;

    @Override
    public void ensureValid(String name, Object value) {
        Long pollTimeout = (Long) value;
        if (pollTimeout == null || pollTimeout < MIN_POLL) {
            throw new ConfigException(name, value, "Poll timeout is invalid, min poll timeout "  + MIN_POLL);
        }
    }
}
