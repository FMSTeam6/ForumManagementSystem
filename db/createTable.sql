create table tags
(
    tag_id  int auto_increment
        primary key,
    content varchar(255) not null
);

create table users
(
    user_id    int auto_increment primary key,
    email      varchar(100) not null UNIQUE,
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null,
    password   varchar(50)  not null,
    username   varchar(50)  not null UNIQUE,
    pictures   BLOB,
    is_admin   BOOLEAN      not null,
    is_banned  BOOLEAN      not null
);

create table admins_phone_number
(
    phone_number varchar(255) null,
    user_id      int          not null
        primary key,
    constraint fk_admins_phone_number_users
        foreign key (user_id) references users (user_id)
);

create table posts
(
    post_id           int auto_increment
        primary key,
    content           varchar(255) null,
    dislikes          int          null,
    likes             int          null,
    timestamp_created datetime(6)  null,
    title             varchar(255) null,
    creator_id        int,
    constraint fk_posts_users
        foreign key (creator_id) references users (user_id)
);

create table comments
(
    comment_id int auto_increment
        primary key,
    text       varchar(255) null,
    user_id    int          null,
    post_id    int          null,
    constraint fk_comments_users
        foreign key (user_id) references users (user_id),
    constraint fk_comments_posts
        foreign key (post_id) references posts (post_id)
);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint fk_posts_tags_tags
        foreign key (tag_id) references tags (tag_id),
    constraint fk_posts_tags_posts
        foreign key (post_id) references posts (post_id)
);

