package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;


import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Field;


public class CSVDecoder<T> implements Decoder<T> {

  @Override
  public T decode(String data, Class type) throws Exception {
    CSVReader reader = new CSVReader(new StringReader(data));
    String[] values = reader.readNext();
    reader.close();

    Field[] fields = type.getDeclaredFields();
    T instance = (T) type.getDeclaredConstructor().newInstance();

    for (int i = 0; i < fields.length && i < values.length; i++) {
      fields[i].setAccessible(true);
      fields[i].set(instance, convert(fields[i].getType(), values[i]));
    }
    return instance;
  }

  private Object convert(Class<?> type, String value) {
    if (type == int.class || type == Integer.class) return Integer.parseInt(value);
    if (type == long.class || type == Long.class) return Long.parseLong(value);
    if (type == double.class || type == Double.class) return Double.parseDouble(value);
    if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
    return value; // String fallback
  }
}
