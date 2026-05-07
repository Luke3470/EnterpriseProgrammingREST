package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YAMLDecoder<T> implements Decoder<T> {

  private final Yaml yaml;

  public YAMLDecoder() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setProcessComments(false);

    this.yaml = new Yaml(options);
  }

  @Override
  public T decode(String data, Class<T> type) {
    return yaml.loadAs(data, type);
  }
}