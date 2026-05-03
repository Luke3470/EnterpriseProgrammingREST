package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;

import com.google.gson.Gson;

public class JSONSerializer<T> implements Serializer<T> {

    private final Gson gson = new Gson();

    @Override
    public String serialize(T object) {
      return gson.toJson(object);
    }

}

