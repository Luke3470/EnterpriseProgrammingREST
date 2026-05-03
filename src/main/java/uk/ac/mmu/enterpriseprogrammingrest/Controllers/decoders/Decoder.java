package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

public interface Decoder<T> {
  T decode(String data, Class<T> type) throws Exception;
}
