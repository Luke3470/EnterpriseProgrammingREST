package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

import com.google.gson.Gson;

public class JSONDecoder implements Decoder {

  private final Gson gson = new Gson();

  @Override
  public Object decode(String data, Class type) throws Exception {
    return gson.fromJson(data, type);
  }
}
