package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.CSVSerializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.JSONSerializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.Serializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.XMLSerializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.CSVDecoder;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.Decoder;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.JSONDecoder;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.XMLDecoder;

public class ContentRegistry {

  private final Map<String, Serializer> serializers = new HashMap<>();
  private final Map<String, Decoder> decoders = new HashMap<>();

  public ContentRegistry() {
    register("application/json", new JSONSerializer(), new JSONDecoder());
    register("application/xml", new XMLSerializer(), new XMLDecoder());
    register("text/csv", new CSVSerializer(), new CSVDecoder());
  }

  private void register(String type, Serializer s, Decoder d) {
    serializers.put(type, s);
    decoders.put(type, d);
  }

  public Serializer getSerializer(String type) {
    return serializers.get(type);
  }

  public Decoder getDecoder(String type) {
    return decoders.get(type);
  }

  public Set<String> supportedTypes() {
    return serializers.keySet();
  }
}