drop table if exists account_tbl;

create table account_tbl
(
    id      int primary key auto_increment,
    user_id int default null,
    money   int default null
);