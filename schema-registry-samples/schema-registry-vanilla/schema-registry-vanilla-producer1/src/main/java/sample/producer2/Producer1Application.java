package sample.producer2;

import com.example.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableBinding(Source.class)
@RestController
public class Producer1Application {

	@Autowired
	private Source source;

	private Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(Producer1Application.class, args);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String sendEmail() {
		source.output().send(MessageBuilder.withPayload(email()).build());
		return "ok, have fun with v1 payload!";
	}
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public String sendSms() {
		source.output().send(MessageBuilder.withPayload(sms()).build());
		return "ok, have fun with v1 payload!";
	}
	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	public String sendNotification() {
		source.output().send(MessageBuilder.withPayload(notification()).build());
		return "ok, have fun with v1 payload!";
	}

	private MessageToSend notification() {
		MessageToSend messageToSend = getMessageToSend();
		PushNotification pushNotification = new PushNotification();
		pushNotification.setArn("google");
		pushNotification.setText("hello");
		messageToSend.setPayload(pushNotification);
		return messageToSend;
	}

	private MessageToSend sms() {
		MessageToSend messageToSend = getMessageToSend();
		Sms sms = new Sms();
		sms.setPhoneNumber("6141231212");
		sms.setText("hello");
		messageToSend.setPayload(sms);
		return messageToSend;
	}

	private MessageToSend email() {
		MessageToSend messageToSend = getMessageToSend();
		Email email = new Email();
		email.setAddressTo("sercan");
		email.setText("hello");
		email.setTitle("hi");
		messageToSend.setPayload(email);
		return messageToSend;
	}

	private MessageToSend getMessageToSend() {
		MessageToSend messageToSend = new MessageToSend();
		messageToSend.setCorrelationId("abc");
		messageToSend.setType("sms");
		return messageToSend;
	}

	//Another convenience POST method for testing deterministic values
	@RequestMapping(value = "/messagesX", method = RequestMethod.POST)
	public String sendMessageX(@RequestParam(value="id") String id, @RequestParam(value="acceleration") float acceleartion,
							   @RequestParam(value="velocity") float velocity, @RequestParam(value="temperature") float temperature) {
		Sensor sensor = new Sensor();
		sensor.setId(id + "-v1");
		sensor.setAcceleration(acceleartion);
		sensor.setVelocity(velocity);
		sensor.setTemperature(temperature);
		source.output().send(MessageBuilder.withPayload(sensor).build());
		return "ok, have fun with v1 payload!";
	}
}

