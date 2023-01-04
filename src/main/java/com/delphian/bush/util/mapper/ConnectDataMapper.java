package com.delphian.bush.util.mapper;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;

public interface ConnectDataMapper<T> {

  /**
   * @param s - Kafka Connect Struct
   * @return POJO from struct.
   */
  T to(Struct s);

  /**
   * @param t - POJO
   * @return Kafka Connect Struct from POJO
   */
  Struct to(T t);

  /**
   * @return Schema of the converter.
   */
  Schema getSchema();
}
