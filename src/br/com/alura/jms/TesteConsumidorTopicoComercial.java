package br.com.alura.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;

import br.com.caelum.modelo.Pedido;

public class TesteConsumidorTopicoComercial {

	public static void main(String[] args) throws Exception {

		InitialContext context = new InitialContext();
		//ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		factory.setTrustAllPackages(true);
		
		Connection connection = factory.createConnection("user", "user");
		connection.setClientID("comercial");
		
		connection.start();
		Session session =  connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topico = (Topic) context.lookup("loja");	
		
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
				
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				
				//TextMessage textMessage = (TextMessage) message;
				ObjectMessage objectMessage = (ObjectMessage) message;
				
				
				try {
					Pedido pedido = (Pedido) objectMessage.getObject();
					System.out.println(pedido.getCodigo());
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
			
		new Scanner(System.in).nextLine();

		connection.close();
		context.close();
	}

}
