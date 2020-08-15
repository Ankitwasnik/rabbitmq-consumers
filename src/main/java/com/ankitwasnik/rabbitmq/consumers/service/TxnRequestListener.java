package com.ankitwasnik.rabbitmq.consumers.service;

import com.ankitwasnik.rabbitmq.consumers.dto.TxnRequest;
import com.ankitwasnik.rabbitmq.consumers.utils.MessageDeserializer;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TxnRequestListener {

  private final MessageDeserializer<TxnRequest> messageDeserializer;

  @Autowired
  public TxnRequestListener(final MessageDeserializer<TxnRequest> messageDeserializer) {
    this.messageDeserializer = messageDeserializer;
  }

  @Bean
  public Consumer<Object> processTxn() {
    return requests -> {
      final List<TxnRequest> transactionRequests;
      try {
        transactionRequests = messageDeserializer.convertToList(requests, TxnRequest.class);
      } catch (final Exception e) {
        log.info("Failed to deserialize txn kpi messages {}", requests);
        throw new RuntimeException("Failed to deserialize messages");
      }
      log.info("Processing {} requests", transactionRequests);

    };
  }

}
