package uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XMLDecoder<T> implements Decoder<T> {

  @Override
  public T decode(String data, Class<T> type) throws Exception {
    JAXBContext context = JAXBContext.newInstance(type);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    return type.cast(unmarshaller.unmarshal(new StringReader(data)));
  }
}
