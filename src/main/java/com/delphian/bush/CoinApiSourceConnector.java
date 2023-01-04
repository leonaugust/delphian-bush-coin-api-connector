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
