package project.service;

public interface CaptchaService {

    boolean isCodeCorrect(String code, String secret);
}
