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

public class PollTimeoutValidator implements ConfigDef.Validator {

  public static final long MIN_POLL = 20;

  /**
   * Ensures the poll timeout is not null and not less than {@value #MIN_POLL}
   *
   * @param name  The name of the configuration
   * @param value The value of the configuration
   */
  @Override
  public void ensureValid(String name, Object value) {
    Long pollTimeout = (Long) value;
    if (pollTimeout == null || pollTimeout < MIN_POLL) {
      throw new ConfigException(name, value,
          "Poll timeout is invalid, min poll timeout " + MIN_POLL);
    }
  }
}
