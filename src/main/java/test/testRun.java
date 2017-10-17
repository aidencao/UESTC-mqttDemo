package test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class testRun {
	public static void main(String[] args) throws Exception{
		testClient.init();
		
		//模拟发送open
		/*MqttClient client = testClient.getClient();
		MqttMessage message = new MqttMessage();
		message.setPayload("open".getBytes());
		client.publish("open/001", message);*/
		testClient.send("open/001", "open");
	}
}
