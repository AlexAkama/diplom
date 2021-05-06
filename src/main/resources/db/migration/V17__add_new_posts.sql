INSERT INTO users(name, email, password, reg_time, is_moderator)
VALUES ('Альф', 'alf1@bk.ru', '$2a$12$/UIcBQmCUGSUxX5nwlGErOlvZVmpxpD9mCfOZmo3DqdZOE5tT16tS', '2021-01-01 01:01:01', 1);

UPDATE posts SET is_active = 1, moderator_id = 13 WHERE moderation_status = 'DECLINED';

ALTER TABLE posts ALTER COLUMN view_count SET DEFAULT 0;

INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 1', 'Почему дети не хотят есть некоторые продукты?', '2021-01-29 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (2, 'Развитие вкуса. Часть 2', 'Про избирательность в еде я уже как-то писала. Сегодня еще немного на эту тему.', '2021-02-04 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (3, 'Развитие вкуса. Часть 3', 'Человек ощущает 5 основных вкусов: сладкий, кислый, горький, солёный и так называемый вкус «умами». Это «мясной» вкус, в частности – глутамата натрия, который часто используется как вкусовая добавка.', '2021-02-11 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (4, 'Развитие вкуса. Часть 4', 'За идентификацию вкуса отвечают луковицы, расположенные на языке, нёбе, щеках и гортани. Их число доходит до 10000 и каждая подает сигналы об определенном вкусе в мозг.', '2021-02-18 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (5, 'Развитие вкуса. Часть 5', 'Этот процесс может быть нарушен, если можем «забьем» вкусовые рецепторы добавками и сахарами. Можно за счет обоняния «обмануть» систему.', '2021-02-25 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 6', 'Существует мнение, что вкусовые рецепторы угасают с возрастом, хотя с этим и спорят современные исследования. Но это уже тема о взрослых.', '2021-03-02 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 7', 'Самой главной проблемой детского возраста является то, что: взрослые могут убедить ребенка во вкусе продукта. При этом на самом деле ребенок может иметь другие ощущения, но доверие к родителям сделает свое дело; сахар может снизить чистоту вкусовых ощущений. Чем больше сахара потребляет ребенок, тем больше будут искажены его вкусовые ощущения; отсутствие продуктов в рационе ребенка приведет к тому, что вкусовые ощущения будут не развиты. В таком случае приучить, например, 10-летку к новой пище будет намного сложнее.', '2021-03-10 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 8', 'Что можно сделать, чтобы развить вкус? Пить воду. Чистую. По норме возраста. Первый стакан выпивать сразу после пробуждения. Чистить язык. Вот прямо щеткой. Можно даже зубной во время утренней и вечерней чистки. Взрослым – отказаться от курения и алкоголя. Детям – от сахара. Проводить профилактику заболеваний органов дыхания и респираторных заболеваний. Сократить потребление соли. Это касается и любых вкусовых добавок. Знакомиться с новыми вкусами. Аккуратно, начиная с маленьких кусочков в разных блюдах. Направлять внимание на еду и ощущения от нее. Отказаться от гаджетов, ТВ и прочих отвлекающих детей факторов. Позволять детям самим формировать впечатление от еды. Не навязывать «вкусно»/ «не вкусно» и т.д.', '2021-03-17 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 9', 'Это можно сделать самим. А дальше.. Вы знаете, к кому обращаться, если будет необходимость.', '2021-03-31 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Развитие вкуса. Часть 10', 'Во Вьетнаме во все добавляют глутамат натрия. И даже приносят его отдельно в плошечке. Чтобы макать в него что-то и ещё больше усиливать вкус.', '2021-04-01 01:01:01', 1, 'NEW');

INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Кардамон', 'Его можно добавлять в выпечку и глинтвейн. Колбасы и пряные маринады приобретут с ним особое звучание. А еще кардамон встречается в сладостях, блюдах из риса, мяса и овощей и особенно ценится в плове.', '2021-04-14 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Корица', 'Незаменимая подруга печённых яблок, пудингов, кофе и глинтвейна. Украсит маринад для рыбы, жаркое из мяса или птицы. Бывает нескольких видов, которые стоит различать, чтобы точнее использовать на кухне', '2021-04-21 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Черный перец', 'Салат, горячее, выпечка, напитки - можно встретить перец везде. Горошек лучше подходит для варки супов, соусов и маринадов. Молотый перец - для салатных приправ, рыбы или мяса.', '2021-04-28 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Паприка', 'Лучше всего копченая. Вкус ее варьируется от острого до сладкого, поэтому она придает своеобразие блюдам многих стран. Рагу, супы, мясо птицы, колбасы, паста, сыры и даже сало – паприка идет ко всему. С ней хорошо запекать и жарить разные виды мяса.', '2021-04-28 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Мускатный орех', 'Особенно часто идет к мясу и выпечке. Хлеб, кексы, крекеры, и даже десерты могут содержать мускатный орех. Он идеально дополняет тыкву, сочетается со шпинатом в начинках и гарнирах, служит добавкой к мясу на углях и блюдам из куриной печени.', '2021-04-28 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Кориандр', 'Он же кинза, когда речь идет только о листьях растения. Семечки кориандра прекрасно дружат с ржаным хлебом и пряниками, рыбой пряного посола, соленьями и квашенными продуктами. Свежий, чуть цитрусовый аромат зернышек поможет разнообразить впечатления от запечённой свинины.', '2021-04-28 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Лавровый лист', 'Супы и пельмени, холодец и соленья, тушенка и рагу, маринованные огурцы и грибы – кажется, нет блюда, в котором бы мы с детства не находили листья лавра. Кстати, рыба при засоле с этой специей становится настоящим деликатесом. А если его растереть в порошок, то можно добавить смесь в колбасы и паштеты.', '2021-04-28 01:01:01', 1, 'NEW');
INSERT INTO posts (user_id, title, text, time, is_active , moderation_status)
VALUES (1, 'Ваниль', 'Десерты и выпечка – друзь ванили. Но ее можно использовать, чтобы подчеркнуть вкус морепродуктов, придать аромат мясу, смягчить кислинку фруктов и ягод. Цельным стручком ванили можно ароматизировать кремы, напитки. А семена ванили добавляют в тесто и соусы', '2021-04-28 01:01:01', 1, 'NEW');

INSERT INTO tags (name) VALUES ('вкус');
INSERT INTO tags (name) VALUES ('приправы');

INSERT INTO tag2post (post_id, tag_id) VALUES (34, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (35, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (36, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (37, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (38, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (39, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (40, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (41, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (42, 8);
INSERT INTO tag2post (post_id, tag_id) VALUES (43, 8);

INSERT INTO tag2post (post_id, tag_id) VALUES (44, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (45, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (46, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (47, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (48, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (49, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (50, 9);
INSERT INTO tag2post (post_id, tag_id) VALUES (51, 9);
