package project.service;

public interface _CaptchaService {

    boolean isCodeCorrect(String code, String secret);
}
