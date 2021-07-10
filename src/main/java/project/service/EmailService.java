package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import project.exception.InternalServerException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * <h2>РЕАЛИЗАЦИЯ СЕРВИСА ОТПРАВКИ ПИСЕМ</h2>
 * {@link EmailService Oбязательные функции}
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String RESTORE_SUBJECT = "Восстановление пароля";
    private static final String RESTORE_TEMPLATE = "<p>Для восстановления пароля перейдите " +
            "<a href=\"%s\">по ссылке</a></p>";
    private static final String CONFIRM_SUBJECT = "Подтверждения email";
    private static final String CONFIRM_TEMPLATE = "<p>Для подтверждения email перейдите " +
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

    /**
     * Отправка простого письма без вложения
     * <br>В теле писмьма можно использовать html-разметку
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param body    тело письма (сообщение)
     */
    public void sendHtmlEmail(String to, String subject, String body) throws InternalServerException {
        if (!checkEmail(to)) return;
        var email = mailSender.createMimeMessage();
        try {
            email.setContent(
                    new String(body.getBytes(StandardCharsets.UTF_8)),
                    "text/html; charset=utf-8");
            var helper = new MimeMessageHelper(email, false, StandardCharsets.UTF_8.name());
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
     * Отправка письма для подвержения нового email
     * @param to адрес получателя
     * @param link сыылка на подтверждение emial
     */
    public void sendConfirmEmail(String to, String link) throws InternalServerException {
        sendHtmlEmail(to, CONFIRM_SUBJECT, String.format(CONFIRM_TEMPLATE, link));
    }

    /**
     * Отправка письма для восслановления пароля
     * @param to адрес получателя
     * @param link ссылка для восстановления пароля
     */
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
    public boolean checkEmail(String email) {
        var pattern = Pattern.compile(".+@.{2,}\\..{2,}");
        var matcher = pattern.matcher(email);
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
