package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

import com.google.gson.Gson;

public class JSONDecoder<T> implements Decoder<T> {

  private final Gson gson = new Gson();

  @Override
  public T decode(String data, Class<T> type) throws Exception {
    return gson.fromJson(data, type);
  }
}
