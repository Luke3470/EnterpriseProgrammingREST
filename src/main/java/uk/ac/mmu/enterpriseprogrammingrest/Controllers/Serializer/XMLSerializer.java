package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer;


import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class XMLSerializer<T> implements Serializer<T> {

  @Override
  public String serialize(T object) throws Exception {
    StringWriter writer = new StringWriter();
    JAXBContext context = JAXBContext.newInstance(object.getClass());
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(object, writer);
    return writer.toString();
  }

}