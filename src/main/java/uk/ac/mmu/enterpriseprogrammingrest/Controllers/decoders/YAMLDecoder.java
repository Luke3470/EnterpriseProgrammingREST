package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

import org.yaml.snakeyaml.Yaml;

public class YAMLDecoder<T> implements Decoder<T> {

  private final Yaml yaml = new Yaml();

  @Override
  public T decode(String data, Class type) {
    return (T) yaml.loadAs(data, type);
  }
}