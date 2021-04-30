package project.dto.post;

import project.dto.main.AppErrorMap;

/**
 * Конструктор ошибок при добавлении нового поста
 */
public class PostErrorMap extends AppErrorMap {

    public void addTitleError() {
        getErrors().put("title", "Заголовок не установлен");
    }

    public void addTextError() {
        getErrors().put("text", "Текст публикации слишком короткий");
    }

}
