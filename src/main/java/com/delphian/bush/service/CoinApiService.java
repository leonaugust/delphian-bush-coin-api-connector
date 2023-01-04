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

package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CoinApiService {

  /**
   * Fetches unread rates from API, filters by offset and orders by offset.
   *
   * @param sourceOffset - The offset connector stopped reading at.
   * @return The rates, ordered by offset, which time is after {@code sourceOffset}
   */
  List<ExchangeRate> getFilteredRates(Optional<String> sourceOffset);

}
