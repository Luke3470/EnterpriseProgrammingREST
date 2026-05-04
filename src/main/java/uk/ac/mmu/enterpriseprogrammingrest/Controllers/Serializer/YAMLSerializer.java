package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;

import org.yaml.snakeyaml.Yaml;

public class YAMLSerializer<T> implements Serializer<T> {

  private final Yaml yaml = new Yaml();

  @Override
  public String serialize(T object) throws Exception {
    return yaml.dump(object);
  }
}