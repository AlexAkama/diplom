package project.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import project.exception.InternalServerException;
import project.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>РЕАЛИЗАЦИЯ СЕРВИСА ОТПРАВКИ ПИСЕМ</h2>
 * {@link EmailService Oбязательные функции}
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final String RESTORE_SUBJECT = "Восстановление пароля";
    private static final String RESTORE_TEMPLATE = "<p>Для восстановления пароля перейдите " +
            "<a href=\"%s\">по ссылке</a></p>";

    /**
     * Инструмент для отпpавки электронной почты
     * <br> автоматически настраиватся по параметрам {@link project.config.EmailConfig конфигурации}
     */
    private final JavaMailSender mailSender;

    /**
     * Адрес электронной почты отправтеля, получаем из настроек проекта (application.yaml)
     */
    @Value("${email.username}")
    private String username;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Отправка простого письма без вложения
     * <br>В теле писмьма можно использовать html-разметку
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param body    тело письма (сообщение)
     */
    @Override
    public void sendHtmlEmail(String to, String subject, String body) throws InternalServerException {
        if (!checkEmail(to)) return;
        MimeMessage email = mailSender.createMimeMessage();
        try {
            email.setContent(
                    new String(body.getBytes(StandardCharsets.UTF_8)),
                    "text/html; charset=utf-8");
            MimeMessageHelper helper = new MimeMessageHelper(email, false, StandardCharsets.UTF_8.name());
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            mailSender.send(email);
        } catch (
                MessagingException e) {
            throw new InternalServerException("Email не отправлен");
        }
    }

    /**
     * Отправка письма для восслановления пароля
     * @param to адрес получателя
     * @param link ссылка для восстановления пароля
     */
    @Override
    public void sendRestorePasswordEmail(String to, String link) throws InternalServerException {
        sendHtmlEmail(to, RESTORE_SUBJECT, String.format(RESTORE_TEMPLATE, link));
    }

    /**
     * Метод для проверки адреса почты
     *
     * @param email адрес почты для проверки
     * @return true - соответсвует, считаем что почта указана корректно
     * <br>false - не соответствует формату, не почта
     */
    @Override
    public boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._-]+@[A-Za-z0-9\\-]+(\\.[A-Za-z0-9\\-]{2,})+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Метод для получения хоста и порта из запроса
     * <br>Используется для формирования обратных ссылок
     *
     * @param request - запрос
     * @return трока в формате host<:port>
     */
    public String getHostAndPort(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

}
