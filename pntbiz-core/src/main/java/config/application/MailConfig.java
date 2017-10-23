package config.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by ucjung on 2017-05-12.
 */
@Configuration
@Component
public class MailConfig {
    static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

    private @Value("#{config['mail.host']}") String host;
    private @Value("#{config['mail.port']}") Integer port;
    private @Value("#{config['mail.username']}") String username;
    private @Value("#{config['mail.password']}") String password;
    private @Value("#{config['mail.charset']}") String charset;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding(charset);

        Properties javaMailProperites = new Properties();
        javaMailProperites.setProperty("mail.smtp.auth", "true");
        javaMailProperites.setProperty("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(javaMailProperites);

        return mailSender;
    }
    /*
	<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
		<property name="host" value="#{config['mail.host']}" />
		<property name="port" value="#{config['mail.port']}" />
		<property name="username" value="#{config['mail.username']}" />
		<property name="password" value="#{config['mail.password']}" />
		<property name="javaMailProperties">
		   <props>
	       	      <prop key="mail.smtp.auth">true</prop>
	       	      <prop key="mail.smtp.starttls.enable">true</prop>
	       	   </props>
		</property>
		<property name="defaultEncoding" value="#{config['mail.charset']}"/>
	</bean>
     */
}
