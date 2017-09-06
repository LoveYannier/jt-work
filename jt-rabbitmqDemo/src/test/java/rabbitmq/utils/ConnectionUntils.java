package rabbitmq.utils;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUntils {

	public static Connection getConn() throws IOException {
		// 链接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 配置链接信息
		factory.setHost("192.168.161.134");
		factory.setPort(5672);

		// 虚拟机名称 与在RabbitMq中注册名称一致
		factory.setVirtualHost("/jt");
		factory.setUsername("jtadmin");
		factory.setPassword("123456");

		// 从工厂中创建链接
		Connection conn = factory.newConnection();

		return conn;
	}

}
