package com.delphian.bush.util.mapper;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;

public interface ConnectDataMapper<T> {
  T to(Struct s);
  Struct to(T t);

  Schema getSchema();
}
