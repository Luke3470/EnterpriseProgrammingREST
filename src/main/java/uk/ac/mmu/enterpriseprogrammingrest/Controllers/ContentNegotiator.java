package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.util.Set;

public class ContentNegotiator {

  public static String negotiate(String header, Set<String> supported) {
    if (header == null || header.isBlank()) {
      return "application/json";
    }

    for (String type : supported) {
      if (header.contains(type)) {
        return type;
      }
    }

    return null;
  }
}