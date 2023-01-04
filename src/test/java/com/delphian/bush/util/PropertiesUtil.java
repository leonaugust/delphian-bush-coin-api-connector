/*
 * MIT License
 *
 * Copyright (c) 2023 Leon Galushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.delphian.bush.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.*;

/**
 * Loads properties from project directory file
 * /config/custom-connector.properties
 */
public class PropertiesUtil {

    static Properties properties = new Properties();

    static {{
        String configFile = System.getProperty("user.dir") + "/config/custom-connector.properties";
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }}

    public static String getProperty(String property) {
        return properties.getProperty(property);
    }

    public static Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();
        properties.stringPropertyNames().forEach(p -> {
            props.put(p, properties.getProperty(p));
        });

        return props;
    }

    public static Map<String, String> getPropertiesOverridden(Map<String, String> overridden) {
        Map<String, String> props = getProperties();
        props.putAll(overridden);

        if (!properties.stringPropertyNames().contains(TOPIC_CONFIG)) {
            props.put(TOPIC_CONFIG, null);
        }
        if (!properties.stringPropertyNames().contains(APPLICATION_CONFIG)) {
            props.put(APPLICATION_CONFIG, null);
        }

        if (!properties.stringPropertyNames().contains(COIN_API_KEY_CONFIG)) {
            props.put(COIN_API_KEY_CONFIG, null);
        }
        if (!properties.stringPropertyNames().contains(PROFILE_ACTIVE_CONFIG)) {
            props.put(PROFILE_ACTIVE_CONFIG, null);
        }
        if (!properties.stringPropertyNames().contains(POLL_TIMEOUT_CONFIG)) {
            props.put(POLL_TIMEOUT_CONFIG, null);
        }
        return props;
    }

    public static String getApiKey() {
        return properties.getProperty(COIN_API_KEY_CONFIG);
    }

}
