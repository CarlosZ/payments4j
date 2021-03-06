package org.payments4j.model;

import org.apache.commons.beanutils.WrapDynaBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for Building Model Objects. Specially useful for tests.
 */
public class AbstractBaseModelBuilder<T> {

  protected Map<String, Object> values = new HashMap<String, Object>();
  protected Class<T> clazz;

  public AbstractBaseModelBuilder(Class<T> clazz) {
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public T build() {
    try {
      WrapDynaBean bean = new WrapDynaBean(clazz.newInstance());
      for (String key : values.keySet()) {
        bean.set(key, values.get(key));
      }
      values.clear();
      return (T) bean.getInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
