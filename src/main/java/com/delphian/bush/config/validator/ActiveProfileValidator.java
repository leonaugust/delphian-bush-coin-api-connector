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

package com.delphian.bush.config.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.Arrays;
import java.util.List;

public class ActiveProfileValidator implements ConfigDef.Validator{

    public static final String TEST_PROFILE = "test";
    public static final String PROD_PROFILE = "prod";

    List<String> availableProfiles = Arrays.asList(TEST_PROFILE, PROD_PROFILE);

    /**
     * Ensures the selected profile is one of {AVAILABLE_PROFILES}
     *
     * @param name  The name of the configuration
     * @param value The value of the configuration
     */
    @Override
    public void ensureValid(String name, Object value) {
        String profile = (String) value;
        if (!availableProfiles.contains(profile)) {
            throw new ConfigException(name, value, "Selected profile is invalid, available profiles "  + availableProfiles);
        }

    }
}
