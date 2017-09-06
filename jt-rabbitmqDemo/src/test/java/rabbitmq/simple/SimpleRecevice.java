package rabbitmq.simple;

import java.io.IOException;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import rabbitmq.utils.ConnectionUntils;

public class SimpleRecevice {
	
	@Test
	public void Receiv() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		//获取链接
		Connection conn = ConnectionUntils.getConn();
		//在连接中创建通道
		Channel channel = conn.createChannel();
		//通道中声明队列
		channel.queueDeclare("test", false, false, false, null);
		//定义队列的消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//监听队列 简单模式（一对一） 自动提交
		channel.basicConsume("test", true, consumer);
		
		//获取消息
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();//有消息 消费端没有关闭
			String msg = new String(delivery.getBody());
			System.out.println("[x] Received "+msg+";");
		}
		
 	}

}
