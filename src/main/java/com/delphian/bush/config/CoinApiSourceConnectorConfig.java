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

package com.delphian.bush.config;

import com.delphian.bush.config.validator.ActiveProfileValidator;
import com.delphian.bush.config.validator.PollTimeoutValidator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static com.delphian.bush.service.CoinApiServiceImpl.TEST_PROFILE;

public class CoinApiSourceConnectorConfig extends AbstractConfig {

  public static final String APPLICATION_CONFIG = "application";

  public static final String APPLICATION_DOC = "Will be used as a partition name";

  public static final String TOPIC_CONFIG = "topic";
  private static final String TOPIC_DOC = "Topic to write to";

  public static final String COIN_API_KEY_CONFIG = "coin.api.key";
  public static final String COIN_API_KEY_DOC = "Specify your crypto panic api key";


  public static final String PROFILE_ACTIVE_CONFIG = "profile.active";

  public static final String PROFILE_DOC = "Which profile is active";


  public static final String POLL_TIMEOUT_CONFIG = "poll.timeout";

  public static final String POLL_TIMEOUT_DOC = "How much time to wait between polls";

  public static final String DEBUG_ADDITIONAL_INFO = "debug.additional.info";
  public static final String DEBUG_ADDITIONAL_INFO_DOC = "Additional debug information enabled";


  public CoinApiSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
    super(config, parsedConfig);
  }

  public CoinApiSourceConnectorConfig(Map<String, String> parsedConfig) {
    this(conf(), parsedConfig);
  }

  /**
   * Defines config properties of the connector.
   *
   * @return ConfigDef with defined properties
   */
  public static ConfigDef conf() {
    return new ConfigDef()
        .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
        .define(APPLICATION_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH,
            APPLICATION_DOC)
        .define(COIN_API_KEY_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH,
            COIN_API_KEY_DOC)
        .define(PROFILE_ACTIVE_CONFIG, ConfigDef.Type.STRING, TEST_PROFILE,
            new ActiveProfileValidator(), ConfigDef.Importance.HIGH, PROFILE_DOC)
        .define(POLL_TIMEOUT_CONFIG, ConfigDef.Type.LONG, 60, new PollTimeoutValidator(),
            ConfigDef.Importance.HIGH, POLL_TIMEOUT_DOC)
        .define(DEBUG_ADDITIONAL_INFO, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW,
            DEBUG_ADDITIONAL_INFO_DOC);
  }
}
