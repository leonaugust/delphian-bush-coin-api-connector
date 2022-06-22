package com.delphian.bush.util.converter;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;

/**
 * origin author @OneCricketeer
 * <a href="https://stackoverflow.com/a/72567414/14308420">...</a>
 */
public interface ConnectPOJOConverter<T> {
  Schema getSchema();
  T fromConnectData(Struct s);
  Struct toConnectData(T t);
}
