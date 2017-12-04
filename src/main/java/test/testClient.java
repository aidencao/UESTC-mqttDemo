package test;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class testClient {
	private static MqttClient client = null;
	
	public static void init() throws Exception {
		String endpoint = "ssl://neutest.mqtt.iot.gz.baidubce.com:1884"; // 输入创建endpoint返回的SSL地址
		String username = "neutest/java_device"; // 输入创建thing返回的username
		String password = "NHTn41OyN54GAC0OTaPcVMacrFU336x9NseCOlbdjd0="; // 输入创建principal返回的password

		/*
		 * String endpoint = "ssl://uestctest.mqtt.iot.gz.baidubce.com:1884";
		 * //输入创建endpoint返回的SSL地址 String username = "uestctest/device_1";
		 * //输入创建thing返回的username String password =
		 * "0e+4RE9ghvs28xIcbHJsyB0jy660njwG4m5yYC36YSk="; //输入创建principal返回的password
		 * String topic = "topic_1"; //订阅的消息主题，本例是指订阅b号楼第五层的温度
		 */
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init((KeyStore) null);
		TrustManager[] trustManagers = tmf.getTrustManagers();

		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, trustManagers, null);

		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);// 设为断线重连后仍能收到短线时的消息
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setSocketFactory(ctx.getSocketFactory());
		options.setAutomaticReconnect(true);// 启动自动重连

		client = new MqttClient(endpoint, String.valueOf(System.currentTimeMillis()));
		client.connect(options);

		// 默认事件处理
		client.setCallback(new MqttCallback() {
			public void connectionLost(Throwable cause) {
				System.out.println("连接失败,原因:" + cause);
				cause.printStackTrace();
			}

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println(
						"接收到消息,来至Topic [" + topic + "] , 内容是:[" + new String(message.getPayload(), "UTF-8") + "],  ");
			}

			public void deliveryComplete(IMqttDeliveryToken token) {
				// 如果是QoS0的消息，token.resp是没有回复的
				System.out.println("消息发送成功! "
						+ ((token == null || token.getResponse() == null) ? "null" : token.getResponse().getKey()));
			}
		});

		// 关注上货信息并给出处理
		client.subscribe("load/#", new IMqttMessageListener() {

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("收到来自“" + topic + "”的消息，消息内容为：" + new String(message.getPayload(), "UTF-8"));
				// 模拟处理
				System.out.println("模拟处理。。。。。。");
				System.out.println("收到来自" + getNum(topic) + "号柜机的上货消息。");

			}
		});

		// 关注关门信息并给出处理
		client.subscribe("close/#", new IMqttMessageListener() {

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("收到来自“" + topic + "”的消息，消息内容为：" + new String(message.getPayload(), "UTF-8"));
				// 模拟处理
				System.out.println("模拟处理。。。。。。");
				System.out.println("收到来自" + getNum(topic) + "号柜机的关门消息。");

			}
		});

		// 关注取货信息并给出处理
		client.subscribe("take/#", new IMqttMessageListener() {

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("收到来自“" + topic + "”的消息，消息内容为：" + new String(message.getPayload(), "UTF-8"));
				// 模拟处理
				System.out.println("模拟处理。。。。。。");
				System.out.println(
						"收到来自" + getNum(topic) + "号柜机的取货消息消息，格子编号为：" + new String(message.getPayload(), "UTF-8"));

			}
		});

		// 关注异常信息并给出处理
		client.subscribe("erro/#", new IMqttMessageListener() {

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("收到来自“" + topic + "”的消息，消息内容为：" + new String(message.getPayload(), "UTF-8"));
				// 模拟处理
				System.out.println("模拟处理。。。。。。");
				System.out.println(
						"收到来自" + getNum(topic) + "号柜机的异常消息消息，异常编号为：" + new String(message.getPayload(), "UTF-8"));

			}
		});
	}

	public static void send(String topic,String content) throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage();
		message.setPayload(content.getBytes());
		client.publish(topic, message);
	}
	
	// 获取主题编号的方法
	private static String getNum(String topic) {
		int i = topic.indexOf('/')+1;
		return topic.substring(i);
	}
}
