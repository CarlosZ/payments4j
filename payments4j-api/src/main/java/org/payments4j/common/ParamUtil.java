package org.payments4j.common;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 */
public class ParamUtil {

  private ParamUtil() {
  }

  public static void requireOption(Map<String, Object> options, String option) {
    checkArgument(options != null && options.get(option) != null, "You must provide a %s.", option);
  }
}
