package project.dto.comment;

import project.dto.main.AppErrorMap;

    /**
     * Конструктор ошибок при добавлении коментария
     */
public class CommentErrorMap extends AppErrorMap {

    public void addTextError(){
        getErrors().put("text", "Текст комментария не задан или слишком короткий");
    }

}
