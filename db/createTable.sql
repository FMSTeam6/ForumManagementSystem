create table tags
(
    tag_id  int auto_increment
        primary key,
    content varchar(255) not null
);

create table users
(
    user_id    int auto_increment
        primary key,
    email      varchar(255) null,
    first_name varchar(255) not null,
    is_admin   bit          null,
    is_banned  bit          null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    username   varchar(255) not null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UK_r43af9ap4edm43mmtq01oddj6
        unique (username)
);

create table admins_phone_number
(
    phone_number varchar(255) null,
    user_id      int          not null
        primary key,
    constraint FKoe6pu3ydoukwttmk4xgd1kwc8
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
    creator_id        int          null,
    constraint FKpbdq30fxpf8l0v3j2eyca7odb
        foreign key (creator_id) references users (user_id)
);

create table comments
(
    comment_id int auto_increment
        primary key,
    text       varchar(255) null,
    user_id    int          null,
    post_id    int          null,
    constraint FK8omq0tc18jd43bu5tjh6jvraq
        foreign key (user_id) references users (user_id),
    constraint FKh4c7lvsc298whoyd4w9ta25cr
        foreign key (post_id) references posts (post_id),
    constraint FKtj5827ll4ht3ccml47m0fss06
        foreign key (comment_id) references posts (post_id)
);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint FK4svsmj4juqu2l8yaw6whr1v4v
        foreign key (tag_id) references tags (tag_id),
    constraint FKcreclgob71ibo58gsm6l5wp6
        foreign key (post_id) references posts (post_id)
);

create table users_comments
(
    User_user_id        int not null,
    comments_comment_id int not null,
    constraint UK_f3c2hlsa7oy34m960153fxlaw
        unique (comments_comment_id),
    constraint FKaqdtr29ba837agch11xdrha11
        foreign key (User_user_id) references users (user_id),
    constraint FKq06lttgwax93n6yee511pfixe
        foreign key (comments_comment_id) references comments (comment_id)
);

create table users_posts
(
    User_user_id  int not null,
    posts_post_id int not null,
    constraint UK_6lrq1pa4b88au1mjd2crlomo1
        unique (posts_post_id),
    constraint FKamro8xqnajs4hlyn42py1p95e
        foreign key (posts_post_id) references posts (post_id),
    constraint FKlnwbys6p3cicturu892h2j63k
        foreign key (User_user_id) references users (user_id)
);

