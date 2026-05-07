package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;


import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YAMLSerializer<T> implements Serializer<T> {

  private final Yaml yaml;

  public YAMLSerializer() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setProcessComments(false);

    this.yaml = new Yaml(options);
  }

  @Override
  public String serialize(T object) throws Exception {
    return yaml.dump(object);
  }
}