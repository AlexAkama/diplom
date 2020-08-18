insert into users(name, email, password, reg_time, is_moderator)
values ('Василиса', 'vasya@gmail.com', '101', '2009-05-09 09:09:09', 0);

insert into users(name, email, password, reg_time, is_moderator)
values ('Елена', 'helen@gmail.com', '111', '2009-05-09 09:09:09', 0);

insert into users(name, email, password, reg_time, is_moderator)
values ('Мария', 'merry@gmail.com', '121', '2009-05-09 09:09:09', 0);

insert into tags(name) values ('мед');
insert into tags(name) values ('питание_в_отпуске');
insert into tags(name) values ('фруктоза');

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Мед или сахар?', 'Так мед или сахар? Что же лучше? Разберёмся?', '2020-01-01 01:01:01', 1, 'ACCEPTED', 12);

insert into tag2post (post_id, tag_id) values (12, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Ложечка меда', 'Представим, что съели ложечку меда. Мозг получил сигнал, что нам сладко, а в организм поступает фруктоза, глюкоза, сахароза, витамины группы В, Е, К, С, каротин и фолиевая кислота. Это если мед произведен не на заводе, конечно.', '2020-06-01 01:01:01', 1, 'ACCEPTED', 13);

insert into tag2post (post_id, tag_id) values (13, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Только не много!', 'Правда, количеством всего этого полезного мы не сможем насладиться, т.к. оно очень мало. Чтобы хоть что-то из микроэлементов повлияло на наше здоровье, мед нужно было бы есть.. банками или даже бочками. А кило меда в неделю - это удар по поджелудочной.', '2020-06-02 01:01:01', 1, 'ACCEPTED', 15);

insert into tag2post (post_id, tag_id) values (14, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Сравнение', 'Сравниваем дальше. Если бы мы съели сахар, то в организм попали бы фруктоза и глюкоза. В столовой ложке меда 64 калории, а сахара - 46.', '2020-06-03 01:01:01', 1, 'ACCEPTED', 18);

insert into tag2post (post_id, tag_id) values (15, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Гликемический индекс', 'Теперь вспомним, что гликемический индекс меда 90, а сахара - 100. Иначе говоря, и то, и другое вызывает в организме сильный отклик и необходимость высоких энергетических затрат на переработку продукта.', '2020-06-04 01:01:01', 1, 'ACCEPTED', 25);

insert into tag2post (post_id, tag_id) values (15, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Тем кто хочет похудеть', 'Более того, если кто-то хочет похудеть, то ни мед, ни сахар вообще не должны попадать в рацион, поскольку оптимальный гликемический индекс продуктов для похудения - до 50.', '2020-06-05 01:01:01', 1, 'ACCEPTED', 37);

insert into tag2post (post_id, tag_id) values (17, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Как лекарство?', 'А если мед рассматривать как лекарство? Добавлять в чай или горячее молоко, например. Тогда надо помнить, что максимальная для меда 45 градусов. Но есть шанс, что это поможет сбить температуру, укрепить сосуды и чуть облегчить состояние больного горла.', '2020-06-06 01:01:01', 1, 'ACCEPTED', 12);

insert into tag2post (post_id, tag_id) values (18, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Так что же лучше? ', 'С моей точки зрения, польза этих продуктов в питании отсутствует. А вот для лечения, может быть, в небольших дозах можно рассматривать мед.', '2020-06-07 01:01:01', 1, 'ACCEPTED', 9);

insert into tag2post (post_id, tag_id) values (19, 5);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (10, 'Помните!!!', 'Но помните, что общая норма сахара для человека - 30 грамм в день. При этом неважно, из каких продуктов вы его получаете.', '2020-06-08 01:01:01', 1, 'ACCEPTED', 17);

insert into tag2post (post_id, tag_id) values (20, 5);



insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (12, 'Фруктоза', 'Дайте знать в комментариях, стоит ли рассказывать отдельно про фруктозу? И как у вас с медом отношения?', '2020-06-09 01:01:01', 1, 'ACCEPTED', 23);

insert into tag2post (post_id, tag_id) values (21, 7);



insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Нова серия - Питание в отпуске', 'Нашла у себя полезную шпаргалку про питание в отпуске. Не могу не поделиться. Сохраняйте и отмечайте в комментариях друзей-туристов.', '2020-06-10 01:01:01', 1, 'ACCEPTED', 41);

insert into tag2post (post_id, tag_id) values (22, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 1', 'Определите свой режим питания. Если вы взрослый человек, пусть это будет 3 полноценных приема пищи и все. Если вы семья с детьми, то ориентируйтесь на их приемы пищи. ', '2020-06-11 01:01:01', 1, 'ACCEPTED', 48);

insert into tag2post (post_id, tag_id) values (23, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 1. Продолжение', 'Но не превращайте весь день в постоянное жевание чего-нибудь. Раз в 3 часа, как правило, достаточно.', '2020-06-12 01:01:01', 1, 'ACCEPTED', 45);

insert into tag2post (post_id, tag_id) values (24, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 2', 'Начинайте первый прием пищи не раньше, чем через час после пробуждения и завершайте приемы пищи за 2 часа до сна. Кофе прекращайте пить до полудня. Все жидкости – за 1-1,5 часа до сна.', '2020-06-14 01:01:01', 1, 'ACCEPTED', 51);

insert into tag2post (post_id, tag_id) values (25, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 3', 'Определитесь заранее, где вы можете питаться. Если в отеле, где питание включено – ок. Если вы питаетесь сами – найдите по карте или отзывам места с подходящим вам, желательно здоровым, меню и продуктами.', '2020-06-14 01:01:01', 1, 'ACCEPTED', 50);

insert into tag2post (post_id, tag_id) values (26, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 3. Продолжение', 'Определите время их работы - это особенно важно, когда вы за рубежом. Простройте маршруты перемещений с учетом всех этих факторов.', '2020-06-14 01:01:01', 1, 'ACCEPTED', 56);

insert into tag2post (post_id, tag_id) values (27, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 4', 'Узнавайте состав того, что вам подают. В особенности речь идет о детских блюдах. Часто в кафе и ресторанах забывают указать, что в состав булочки входят орехи, крем или шоколад внутри, а в салат добавлен сыр или сметана. ', '2020-06-30 01:01:01', 1, 'ACCEPTED', 52);

insert into tag2post (post_id, tag_id) values (28, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 4. Продолжение', 'Уточняйте все детали у сотрудников, и, если нужно, просите что-то убрать, добавить или приготовить отдельно. Могу сказать, что мы делаем так постоянно, и нам практически никогда не отказывают.', '2020-06-30 01:01:01', 1, 'ACCEPTED', 56);

insert into tag2post (post_id, tag_id) values (29, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 5', 'Уделяйте внимание напиткам. Выбор - в пользу тех, которые содержат натуральные составляющие. Между морсом собственного производства и промышленным лимонадом лучше сделать выбор в пользу первого. ', '2020-06-30 01:01:01', 1, 'ACCEPTED', 56);

insert into tag2post (post_id, tag_id) values (30, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 5. Продолжение', 'А еще лучше – взять простую воду. Можно прохладную, но не ледяную.', '2020-06-30 01:01:01', 1, 'ACCEPTED', 58);

insert into tag2post (post_id, tag_id) values (31, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 6', 'Ну и, конечно, даже в жару и в отеле all inc не стоит увлекаться алкоголем. Хорошо будет в моменте, а последствия долетят до вас позже. Отдавайте предпочтение сухим винам, легким некалорийным алкогольным напиткам.', '2020-06-30 01:01:01', 1, 'ACCEPTED', 54);

insert into tag2post (post_id, tag_id) values (32, 6);

insert into posts (user_id, title, text, time, is_active , moderation_status, view_count)
values (11, 'Питание в отпуске. Часть 6. Продолжение', 'Сместите время приема алкоголя на вечер, когда палящее солнце уходит в закат, растягивайте и смакуйте, вместо того, чтобы выпивать залпом, и старайтесь принимать алкоголь на сытый желудок.', '2020-06-30 01:01:01', 1, 'ACCEPTED', 56);

insert into tag2post (post_id, tag_id) values (33, 6);
