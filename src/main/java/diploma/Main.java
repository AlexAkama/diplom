package diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

}

// Этап подготовительный
// Создать Git-репозиторий локально, также желательно использовать удаленный репозиторий на github.com
// (чтобы случайно не потерять результаты своей работы при форс-мажорных ситуациях). - СДЕЛАНО

// Этап 1
// Создать новое Spring-приложение, аналогичное приложению BookLibrary,
// которое вы разрабатывали в рамках модуля 12:
//      - Подключить с помощью Maven необходимые зависимости. - СДЕЛАНО
//      - Создать класс и метод Main, в котором должно запускаться Spring-приложение:
//              SpringApplication.run(Main.class, args); - СДЕЛАНО
//      - Создать файл конфигурации application.yml - СДЕЛАНО
//      - Создать в пакете controller классы: - СДЕЛАНО
//          • DefaultController - для обычных запросов не через API (главная страница - /, в частности)
//          • ApiPostController - обрабатывает все запросы /api/post/*
//          • ApiAuthController - обрабатывает все запросы /api/auth/*
//          • ApiGeneralController - для прочих запросов к API.

// Подключить к приложению frontend, разместив содержимое архива “dist.zip” в подпапках папки
// “resources” - аналогично тому, как это сделано в проекте BookLibrary: CSS- и JS-файлы,
// а также изображения в папках /resources/static/css, /resources/static/js и /resources/static/img
// соответственно, а файл index.html - в папке /resources/templates/.

// Сделать так, чтобы при входе на главную страницу открывался шаблон index.html
// (аналогично тому, как это сделано в проекте “BookLibrary”). - СДЕЛАНО

// На этом этапе необходимо слить изменения в ветку master и отправить на проверку преподавателю.

// Этап 2
// Создать структуру базы данных с помощью сущностей Hibernate в пакете model,
// по примеру проекта “BookLibrary”. Структура базы данных описана в файле “db.pdf”. - СДЕЛАНО

// На этом этапе необходимо слить изменения в ветку master и отправить на проверку.

// TODO Этап 3
// Реализовать сначала методы API для получения информации о блоге(/api/init) и
// для получения постов(/api/post),а затем и все остальные методы API в соответствии
// с документацией в файле “api.pdf”. Для ускорения разработки приложения вы можете
// тестировать API с помощью таких сервисов как Talend API Tester-Free Edition
// (расширение для браузера Google Chrome)или приложение Postman(Win/Mac OS/Linux).

// МИГРАЦИИ
// - миграции global_settings - СДЕЛАНО
// - миграции с тестовыми данными
//      - юзеры - СДЕЛАНО
//      - посты - СДЕЛАНО
//      - комменты - СДЕЛАНО
//      - таг - СДЕЛАНО
//      - связать тег-пост - СДЕЛАНО
//      - лайки - СДЕЛАНО

//TODO ОБРАБОТЧИКИ
// - init - данные из application.yml - СДЕЛАНО
// - settings - из global_settings - СДЕЛАНО
// - check - пока заглушку с false - СДЕЛАНО
// - post - СДЕЛАНО
// - search (будем искать в имени, заголовке, тексте, коменте и теге) - СДЕЛАНО
// - tag - доделать вес тега!!!

// добавить в миграции постов что бы проверить разделение на страницы - СДЕЛАНО

// Сделать так,чтобы при открытии вашего приложения по ссылкам(например,/post/534/)
// тоже выдавался файл index.html. Frontend-приложение при этом само определит,
// какую информацию нужно загрузить.В случае,если запрошена страница по адресу,
// который не существует(например,какой-нибудь/fhdsjfhkjsdf/), переадресовывать на страницу/404/,
// на которой выдавать всё ту же страницу index.html с кодом ответа 404.
// Frontend-приложение автоматически определит,по какому адресу был запрос,и отобразит ошибку 404.

// видимо отправить преподаваьелю

// Размещение проекта для демонстрации на защите