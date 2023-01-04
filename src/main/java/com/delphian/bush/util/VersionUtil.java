package com.delphian.bush.util;

public class VersionUtil {

  public static final Integer FIRST_VERSION = 1;


  /**
   * @return - the version of the connector.
   */
  public static String getVersion() {
    try {
      return VersionUtil.class.getPackage().getImplementationVersion();
    } catch (Exception ex) {
      return "0.0.1";
    }
  }

}
