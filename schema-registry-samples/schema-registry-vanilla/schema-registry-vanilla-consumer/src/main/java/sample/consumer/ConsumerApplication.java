package sample.consumer;

import com.example.*;
import org.apache.avro.generic.GenericData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

import java.io.IOException;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.*;

@SpringBootApplication
@EnableBinding(Sink.class)
@EnableSchemaRegistryClient
public class ConsumerApplication {
    ObjectMapper objectMapper = new ObjectMapper();
    private final Log logger = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void process(MessageToSend data) {
        Match(data.getPayload()).of(
                Case($(instanceOf(Sms.class)), sms -> {
                    System.out.println("handled sms");
                    return toString(sms);
                }),
                Case($(instanceOf(Email.class)), email -> {
                    System.out.println("handled email");
                    return toString(email);
                }),
                Case($(instanceOf(PushNotification.class)), notification -> {
                    System.out.println("handled push notification");
                    return toString(notification);
                })
        );
        logger.info(data);
    }

    private String toString(Object sms) {
        try {
            return objectMapper.writeValueAsString(sms);
        } catch (IOException e) {
            return "null";
        }
    }
}