use forum;

-- data tags

INSERT INTO tags(tag_id, content)VALUES (1,'Content1');
INSERT INTO tags(tag_id, content)VALUES (2,'Content2');
INSERT INTO tags(tag_id, content)VALUES (3,'Content3');
INSERT INTO tags(tag_id, content)VALUES (4,'Content4');
INSERT INTO tags(tag_id, content)VALUES (5,'Content5');

-- data users

INSERT INTO users(user_id, first_name, last_name, email, username, password, is_admin, is_banned)
VALUES (1,'Ivan','Ivanov','ivan@abv.bg','ivancho','ivan123',true,false);
INSERT INTO users(user_id, first_name, last_name, email, username, password, is_admin, is_banned)
VALUES (2,'Pesho','Peshov','pesho@abv.bg','pesho1','pesho123',false,false);
INSERT INTO users(user_id, first_name, last_name, email, username, password, is_admin, is_banned)
VALUES (3,'Nadya','Naidenova','nadya@gmail.com','nadya1','nadya123',false,false);
INSERT INTO users(user_id, first_name, last_name, email, username, password, is_admin, is_banned)
VALUES (4,'Gergana','Nikolova','geri@gmail.com','gery1','gery123',false,true);
INSERT INTO users(user_id, first_name, last_name, email, username, password, is_admin, is_banned)
VALUES (5,'Maria','Atanasova','mery@gmail.com','mery1','mery123',false,false);

-- data posts

INSERT INTO posts(post_id, title, content, likes, dislikes, timestamp_created, creator_id)
VALUES (1,'FirstPost','This is a first post',1,0,'2024-01-23 12:08:56',1);
INSERT INTO posts(post_id, title, content, likes, dislikes, timestamp_created, creator_id)
VALUES (2,'SecondPost','This is a second post',0,0,'2023-12-23 13:08:56',2);
INSERT INTO posts(post_id, title, content, likes, dislikes, timestamp_created, creator_id)
VALUES (3,'FirstPost','This is a first post',2,1,'2023-04-22 15:08:56',3);
INSERT INTO posts(post_id, title, content, likes, dislikes, timestamp_created, creator_id)
VALUES (4,'ThirdPost','This is a third post',0,1,'2024-01-22 13:08:56',4);
INSERT INTO posts(post_id, title, content, likes, dislikes, timestamp_created, creator_id)
VALUES (5,'SecondPost','This is a second post',1,1,'2024-01-12 15:08:56',5);

-- data comments

INSERT INTO comments(comment_id, text, user_id, post_id)
VALUES (1,'First Comment',1,1);
INSERT INTO comments(comment_id, text, user_id, post_id)
VALUES (2,'Second Comment',2,2);
INSERT INTO comments(comment_id, text, user_id, post_id)
VALUES (3,'First Comment',3,1);
INSERT INTO comments(comment_id, text, user_id, post_id)
VALUES (4,'Second Comment',4,3);
INSERT INTO comments(comment_id, text, user_id, post_id)
VALUES (5,'First Comment',5,4);

-- data posts_tags

INSERT INTO posts_tags(post_id, tag_id)
VALUES (1,5);
INSERT INTO posts_tags(post_id, tag_id)
VALUES (2,4);
INSERT INTO posts_tags(post_id, tag_id)
VALUES (3,1);
INSERT INTO posts_tags(post_id, tag_id)
VALUES (4,2);
INSERT INTO posts_tags(post_id, tag_id)
VALUES (5,3);

-- data admins_phone_number
INSERT INTO admins_phone_number(user_id, phone_number)
VALUES (1,'123456789');