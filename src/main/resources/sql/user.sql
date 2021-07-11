drop table if exists user_tbl;

create table user_tbl
(
    id    int primary key auto_increment,
    name  varchar(30) default null,
    age   int         default null,
    email varchar(30) default null
);

insert into user_tbl(name, age, email)
values ('test1', 11, 'test1@example.com'),
       ('test2', 12, 'test2@example.com'),
       ('test3', 13, 'test3@example.com'),
       ('test4', 14, 'test4@example.com'),
       ('test5', 15, 'test5@example.com');
