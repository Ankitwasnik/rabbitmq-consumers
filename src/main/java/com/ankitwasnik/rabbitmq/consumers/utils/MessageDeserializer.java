package com.ankitwasnik.rabbitmq.consumers.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MessageDeserializer<T> {

  private final ObjectMapper objectMapper;

  public MessageDeserializer() {
    objectMapper = initObjectMapper();
  }

  private ObjectMapper initObjectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public List<T> convertToList(final Object messages, final Class<T> type) throws Exception {
    try {
      if (messages instanceof String) {
        final T message = objectMapper.readValue((String) messages, type);
        return Arrays.asList(message);
      } else if (messages instanceof Collection) {
        return getListElements((ArrayList<?>) messages, type);
      }
    } catch (final Exception e) {
      throw new Exception("Failed to convert messages to type:" + type);
    }
    throw new Exception("Unsupported message type");
  }

  private List<T> getListElements(final ArrayList<?> messages, final Class<T> type) throws IOException {
    final List<T> list = new ArrayList<>();
    for (final Object value : messages) {
      final T t = convertSingleElement(type, value);
      list.add(t);
    }
    return list;
  }

  private T convertSingleElement(final Class<T> type, final Object value) throws IOException {
    if (value instanceof byte[]) {
      return objectMapper.readValue((byte[]) value, type);
    } else if (value instanceof String) {
      return objectMapper.readValue((String) value, type);
    }
    return objectMapper.convertValue(value, type);
  }
}
