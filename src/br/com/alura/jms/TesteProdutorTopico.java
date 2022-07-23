package br.com.alura.jms;

import java.io.StringWriter;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import org.apache.activemq.ActiveMQConnectionFactory;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TesteProdutorTopico {

	public static void main(String[] args) throws Exception {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection("user", "user");
		connection.start();
		
		Session session =  connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination topico = (Destination) context.lookup("loja");	
		MessageProducer producer = session.createProducer(topico);
			
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
//		StringWriter writer = new StringWriter();
//		JAXB.marshal(pedido, writer);
//		String xml = writer.toString();
//		System.out.println(xml);
		
		Message message = session.createObjectMessage(pedido);
		//Message message = session.createTextMessage(xml);
		//message.setBooleanProperty("ebook", true);
		producer.send(message);

		connection.close();
		context.close();
	}

}
