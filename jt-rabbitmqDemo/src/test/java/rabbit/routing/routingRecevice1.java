package rabbit.routing;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import rabbitmq.utils.ConnectionUntils;

public class routingRecevice1 {
	
	private final static String EXCHANGE_NAME = "jt_test_exchange_direct";
	private final static String QUEUE_NAME="test_queue_exchange1";
	@Test
	public void Rece1() throws Exception{
		
		Connection conn = ConnectionUntils.getConn();
		Channel channel = conn.createChannel();
		//声明队列 并且绑定交换机
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key");
		
		//同一时刻服务器只能发送一条消息给消费者
		channel.basicQos(1);
		
		//声明消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//监听 false手动返回完成
		channel.basicConsume(QUEUE_NAME, false, consumer);
		
		//获取消息
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("[x] Recevice "+msg+";");
			
			//返回消费状态
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
		}
		
	}

}
