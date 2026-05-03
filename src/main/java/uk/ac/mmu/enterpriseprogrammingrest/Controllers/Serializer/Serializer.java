package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;

public interface Serializer<T> {
  String serialize(T object) throws Exception;
}
