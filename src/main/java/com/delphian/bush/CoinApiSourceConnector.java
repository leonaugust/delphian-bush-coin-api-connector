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

package com.delphian.bush;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.util.VersionUtil;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoinApiSourceConnector extends SourceConnector {
    private CoinApiSourceConnectorConfig config;

    /**
     * @inheritDoc
     */
    @Override
    public void start(Map<String, String> props) {
        config = new CoinApiSourceConnectorConfig(props);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<? extends Task> taskClass() {
        return CoinApiSourceTask.class;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>(1);
        configs.add(config.originalsStrings());
        return configs;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void stop() {
        // Nothing to do since no background monitoring is required
    }

    /**
     * @inheritDoc
     */
    @Override
    public ConfigDef config() {
        return CoinApiSourceConnectorConfig.conf();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String version() {
        return VersionUtil.getVersion();
    }
}
