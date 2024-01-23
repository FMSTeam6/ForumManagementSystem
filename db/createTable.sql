create table tags (

                      tag_id int auto_increment primary key,

                      content varchar (100) not null

);

create table users(

                      user_id int auto_increment primary key,

                      first_name varchar (50) not null,

                      last_name varchar (50) not null,

                      email varchar (100) not null unique,

                      username varchar (100) not null,

                      password varchar (100) not null,

                      isAdmin boolean not null,

                      isBanned boolean not null


);

create table admins_phone_number(

                                    user_id int primary key not null,

                                    phone_number varchar(255) not null,

                                    constraint admins_phone_number_user_id

                                        foreign key (user_id) references users(user_id)

);

create table posts (

                       post_id int auto_increment primary key,

                       title varchar (64) not null,

                       content TEXT not null,

                       likes int,

                       dislikes int,

                       timestamp_created DateTime not null,

                       creator_id int not null,

                       constraint posts_users_user_id

                           foreign key (creator_id) references users(user_id)

);

create table comments(

                         comment_id int auto_increment primary key,

                         text TEXT not null,

                         author_id int not null,

                         post_id int not null,

                         constraint comments_users_id

                             foreign key (author_id) references users(user_id),

                         constraint comments_posts_id

                             foreign key (post_id) references posts(post_id)

);


create table posts_tags (

                            post_id int not null,

                            tag_id int not null,

                            constraint posts_tags_posts

                                foreign key (post_id) references posts(post_id),

                            constraint posts_tags_tags

                                foreign key (tag_id) references tags(tag_id)


);