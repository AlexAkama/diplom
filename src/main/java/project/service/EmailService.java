package project.service;

import project.exception.InternalServerException;

import javax.servlet.http.HttpServletRequest;

/**
 * <h2>ОБЯЗАТЕЛЬНЫЕ ФУНКЦИИ ПОЧТОВОГО СЕРВИСА</h2>
 * <tr><td>{@link EmailService#sendHtmlEmail sendHtmlEmail}</td><td> - отправка HTML письма</td></tr>
 * <tr><td>{@link EmailService#sendRestorePasswordEmail}</td><td> - отправка письма для востановления пароля</td></tr>
 * <tr><td>{@link EmailService#checkEmail}</td><td> - проверка валидности email</td></tr>
 * <tr><td>{@link EmailService#getHostAndPort}</td><td> - получение хоста и порта из запроса</td></tr>
 */
public interface EmailService {

    void sendHtmlEmail(String to, String subject, String body) throws InternalServerException;

    void sendRestorePasswordEmail(String to, String link) throws InternalServerException;

    boolean checkEmail(String email);

    String getHostAndPort(HttpServletRequest request);

}
