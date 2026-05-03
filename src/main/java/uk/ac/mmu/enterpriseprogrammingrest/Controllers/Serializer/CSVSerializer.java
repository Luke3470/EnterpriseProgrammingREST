package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;

import com.opencsv.CSVWriter;
import java.io.*;
import java.lang.reflect.Field;

public class CSVSerializer<T> implements Serializer<T> {

  @Override
  public String serialize(T object) throws Exception {
    StringWriter out = new StringWriter();
    CSVWriter writer = new CSVWriter(out);

    Field[] fields = object.getClass().getDeclaredFields();
    String[] values = new String[fields.length];

    for (int i = 0; i < fields.length; i++) {
      fields[i].setAccessible(true);
      Object value = fields[i].get(object);
      values[i] = value == null ? "" : value.toString();
    }

    writer.writeNext(values);
    writer.close();
    return out.toString();
  }

  private Object convert(Class<?> type, String value) {
    if (type == int.class || type == Integer.class) return Integer.parseInt(value);
    if (type == long.class || type == Long.class) return Long.parseLong(value);
    if (type == double.class || type == Double.class) return Double.parseDouble(value);
    if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
    return value; // String fallback
  }
}