package test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class testRun {
	public static void main(String[] args) throws Exception{
		testClient.init();
		
		testClient.send("open/001", "open");
	}
}
