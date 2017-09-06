package rabbit.routing;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import rabbitmq.utils.ConnectionUntils;

public class routingRecevice2 {
	private final static String EXCHANGE_NAME = "jt_test_exchange_fanout";
	private final static String QUEUE_NAME="test_queue_exchange";
	
	@Test
	public void Rece2() throws Exception{
		//获取链接和通道
		Connection conn = ConnectionUntils.getConn();
		Channel channel = conn.createChannel();
		
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定队列到指定交换机上
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key2");
		
		//同一时刻服务器只能发送一条消息给消费者
		channel.basicQos(1);
		//new消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, false, consumer);
		
		//消费消息
		while(true){
			//一条一条的获取传送过来的消息
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("[x] Recevice "+msg);
			Thread.sleep(100);
			
			//返回消费状态
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
		}
	}

}
