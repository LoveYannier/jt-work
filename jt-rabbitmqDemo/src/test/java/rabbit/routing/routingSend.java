package rabbit.routing;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import rabbitmq.utils.ConnectionUntils;

public class routingSend {
	private final static String EXCHANGE_NAME = "jt_test_exchange_direct";
	@Test	
	public void Send() throws Exception{
		//开启链接和通道
		Connection conn = ConnectionUntils.getConn();
		Channel channel = conn.createChannel();
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME,"direct");
		
		//消息的发送
		String msg = "陈哲";
		
		//制定routingKey
		channel.basicPublish(EXCHANGE_NAME, "key", null, msg.getBytes());
		System.out.println("[x] Send "+msg +";");
		
		channel.close();
		conn.close();
		
	}

}
