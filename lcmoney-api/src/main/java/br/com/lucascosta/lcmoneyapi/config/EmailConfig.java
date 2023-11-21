package br.com.lucascosta.lcmoneyapi.config;

import br.com.lucascosta.lcmoneyapi.config.property.LcmoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Autowired
    private LcmoneyApiProperty lcmoneyApiProperty;

    @Bean
    public JavaMailSender javaMailSender() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.connectiontimeout", 10000);

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setHost(lcmoneyApiProperty.getMail().getHost());
        javaMailSender.setPort(lcmoneyApiProperty.getMail().getPort());
        javaMailSender.setUsername(lcmoneyApiProperty.getMail().getUsername());
        javaMailSender.setPassword(lcmoneyApiProperty.getMail().getSenha());

        return javaMailSender;
    }
}
