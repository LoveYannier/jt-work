package rabbitmq.simple;
import java.io.IOException;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import rabbitmq.utils.ConnectionUntils;
public class SimpleSend {
	
	/**
	 * 步骤：
	 * 1、链接到rabbitMq的server上 链接到虚拟机vhost
	 * 2、创建一个通道Channel 、
	 * 3、声明或绑定消息队列Queue
	 * 4、发送消息到消息队列中
	 * @throws IOException 
	 */
	@Test
	public void send() throws IOException{
		
		Connection conn = ConnectionUntils.getConn();
		
		//创建通道
		Channel channel = conn.createChannel();
		
		//声明（绑定）队列
		channel.queueDeclare("test", false, false, false, null); //固定格式：队列名称 false false false null
		
		//消息内容
		String msg ="Hello RabbitMq";
		
		//发送消息
		channel.basicPublish("", "test", null, msg.getBytes()); //固定格式： 空字符串 队列名称 null 消息内容
		System.out.println("[x] Sent " + msg +";");
		//关闭通道和链接
		channel.close();
		conn.close();
		
	}
	
}
