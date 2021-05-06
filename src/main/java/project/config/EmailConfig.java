package project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * <h2>КОНФИРУРАЦИЯ СЕРВИСА ОТПРАВКИ ПИСЕМ</h2>
 * Конфигурация для {@link project.service.EmailService email-сервиса}
 */
@Configuration
public class EmailConfig {

    /**
     * Хост для отправки писем, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.host}")
    private String host;

    /**
     * Порт доступа, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.port}")
    private int port;

    /**
     * Адрес этектронной почты для отправки, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.username}")
    private String username;

    /**
     * Пароль доступа для указанного выше адреса, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.password}")
    private String password;

    /**
     * Создание и настройка {@link JavaMailSender инстумента для отпавки электронной почты}
     *
     * @return возвращает новый настроеный {@link JavaMailSender}
     */
    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());

        Properties props = mailSender.getJavaMailProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.debug", "true");

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
