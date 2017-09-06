package rabbitmq.work;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import rabbitmq.utils.ConnectionUntils;

public class WorkRecevice2 {
	
	private static final String QUEUE_NAME = "test_queue_work";
	@Test
	public void Recevi() throws Exception{
		Connection conn = ConnectionUntils.getConn();
		Channel channel = conn.createChannel();
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//制定 同一时刻服务器只会发送一条消息给消费者
		channel.basicQos(1);
		
		//定义队列的消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//监听队列
		channel.basicConsume(QUEUE_NAME, false, consumer);
		//获取消息
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("[x] Recevice "+msg+";");
			//休眠测试
			Thread.sleep(1000);
			//返回确认状态
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
		}
	}
	

}
