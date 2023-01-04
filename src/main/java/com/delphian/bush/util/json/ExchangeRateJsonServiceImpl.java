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

package com.delphian.bush.util.json;

import com.delphian.bush.dto.ExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ExchangeRateJsonServiceImpl {

  private static final Logger log = LoggerFactory.getLogger(ExchangeRateJsonServiceImpl.class);

  private final ObjectMapper objectMapper;
  private final InputStream inputStream;
  private final Class<ExchangeRateResponse> targetClass;

  public ExchangeRateJsonServiceImpl(ObjectMapper objectMapper) throws IOException {
    this.objectMapper = objectMapper;
    this.inputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("mocked-stats.json");
    this.targetClass = ExchangeRateResponse.class;
  }

  /**
   * @return The rates from test file <a href="file:../resources/mocked-stats.json</a>
   */
  public ExchangeRateResponse getFromJson() {
    try {
      return objectMapper.readValue(inputStream, targetClass);
    } catch (IOException e) {
      log.debug("Json test service encountered unexpected exception: {}", e.getMessage());
      return null;
    }
  }


}
