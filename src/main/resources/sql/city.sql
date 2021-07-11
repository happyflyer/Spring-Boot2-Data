drop table if exists city_tbl;

create table city_tbl
(
    id      int primary key auto_increment,
    name    varchar(30) default null,
    state   varchar(30) default null,
    country varchar(30) default null
);