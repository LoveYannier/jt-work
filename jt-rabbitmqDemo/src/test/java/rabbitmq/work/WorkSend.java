package rabbitmq.work;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import rabbitmq.utils.ConnectionUntils;

public class WorkSend {

	private static final String QUEUE_NAME = "test_queue_work";

	@Test
	public void WorkSend() throws Exception {
		// 创建链接和通道
		Connection conn = ConnectionUntils.getConn();
		Channel channel = conn.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 发送消息
		for (int i = 0; i < 100; i++) {
			String msg = "Working mode" + i;
			// 消息发送
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			
			System.out.println("[x] Sent "+msg+";");
			
//			Thread.sleep(i*10);
		}
		
		channel.close();
		conn.close();

	}

}
