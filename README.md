# bentoExercise
訂購便當練習
Spring boot + easyui + Spring Data JPA

CREATE DATABASE testbento;

CREATE TABLE testbento.member
(id integer not null auto_increment,
account_name char(50) not null comment '帳號名稱',
member_name char(50) not null comment '姓名',
password char(200) not null comment '密碼',
author_id int(90) not null comment '權限ID',
add_time datetime comment '新增時間',
update_time datetime comment '更新時間',
primary key (id));

INSERT INTO testbento.member
(account_name, member_name, password, author_id, add_time, update_time)
VALUES('admin', '超級管理', '$2a$10$0ETtZwpYbemPzxAtsPdsze5tQ5kKXiwEbhaMv99AswK1oizKgU1c.', 1, '2022-06-02 05:35:17', NULL);

INSERT INTO testbento.member
(account_name, member_name, password, author_id, add_time, update_time)
VALUES('TEST999', '測試帳號9999', '$2a$10$iGNtWYmL0mXfnCTkXQKW8eJc96MttzX6u3R3jjzwGyr4VC9iPAy3u', 2, '2022-05-27 12:34:26', null);


create table testbento.bento_menu (
menu_id integer not null auto_increment,
product_name varchar(30) not null comment '便當名稱',
price integer not null comment '價格',
shelf_status integer not null comment '上下架',
sell_day integer not null comment '每日可販賣數量',
sell_person integer not null comment '每人可購買數量',
add_time datetime comment '新增時間',
update_time datetime comment '更新時間',
primary key (menu_id)
);

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '排骨飯', 80, 1, 50 , 1, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '雞排飯', 70, 1, 80 , 2, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '控肉飯', 80, 1, 60 , 3, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '滷肉飯', 60, 1, 30 , 3, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '招牌飯', 60, 1, 30 , 3, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value( '菜飯', 80, 1, 100 , 5, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value('香腸飯', 80, 1, 100 , 5, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value('三寶飯', 70, 1, 50 , 2, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value('雞腿飯', 90, 1, 30 , 3, now());

insert into testbento.bento_menu (product_name , price, shelf_status , sell_day, sell_person , add_time)
value('鱈魚飯', 70, 1, 30 , 1, now());

CREATE TABLE testbento.navigation_menu
(id integer not null auto_increment,
parentId integer comment '父層ID',
name char(30) comment '頁面名稱',
isOnlyAdmin integer comment '1為管理層可見，0為全部可見',
sortOrder integer comment '排序',
isParent integer comment '是否為父階層',
url char(30) comment 'url',
primary key (id));

insert into testbento.navigation_menu (parentId, name, isOnlyAdmin, sortOrder, isParent, url) value (0,'訂購便當',0,1,0,'purchase/orderBento');
insert into testbento.navigation_menu (parentId, name, isOnlyAdmin, sortOrder, isParent, url) value (0,'帳號管理',1,1,0,'member/memberList');
insert into testbento.navigation_menu (parentId, name, isOnlyAdmin, sortOrder, isParent, url) value (0,'便當品項管理',1,1,0,'bento/bentoMenu');
insert into testbento.navigation_menu (parentId, name, isOnlyAdmin, sortOrder, isParent, url) value (0,'訂單管理',1,1,0,'order/orderList');

create table testbento.order_list (
order_id integer not null auto_increment,
order_number varchar(50) comment '訂單編號',
order_account char(30) comment '訂購帳號',
order_name char(30) comment '訂購姓名',
total_price integer comment '總金額',
order_time datetime comment '訂購時間',
primary key (order_id),
unique (order_number));


create table testbento.order_detail (
detail_id integer not null auto_increment,
order_id integer comment 'fk from order_list : order_number',
menu_id integer comment 'fk from bento_menu : menu_id',
order_count integer comment '購買數量',
total_price integer comment 'menu_price * order_count',
order_time datetime comment '訂購時間',
primary key (detail_id),
foreign key (order_id) references order_list(order_id),
foreign key (menu_id) references bento_menu(menu_id));
    

CREATE TABLE testbento.authority (
 id integer NOT NULL AUTO_INCREMENT,
 name char(50) NOT NULL comment '權限名稱',
 add_time datetime DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT authority_pkey PRIMARY KEY (id),
 CONSTRAINT uk_authority_name UNIQUE KEY(name)
);

INSERT INTO testbento.authority
(name) VALUES ('Admin');
INSERT INTO authority
(name) VALUES ('Normal');


create table testbento.author_menu (
id integer not null auto_increment,
navigation_id integer not null comment 'fk from navigation_menu',
author_id integer not null comment 'fk from authority',
primary key (id),
foreign key (navigation_id) references navigation_menu(id),
foreign key (author_id) references authority(id)
);

create table testbento.member_author (
id integer not null auto_increment,
member_id integer not null comment 'fk from member',
author_id integer not null comment 'fk from authority',
primary key (id),
foreign key (member_id) references member(id),
foreign key (author_id) references authority(id)
);

INSERT INTO testbento.author_menu
(navigation_id, author_id)
VALUES( 1, 1);
INSERT INTO testbento.author_menu
(navigation_id, author_id)
VALUES(2, 1);
INSERT INTO testbento.author_menu
(navigation_id, author_id)
VALUES(3, 1);
INSERT INTO testbento.author_menu
(navigation_id, author_id)
VALUES(4, 1);
INSERT INTO testbento.author_menu
(navigation_id, author_id)
VALUES(1, 2);

INSERT INTO testbento.member_author
(id, member_id, author_id)
VALUES(1, 1, 1);
INSERT INTO testbento.member_author
(id, member_id, author_id)
VALUES(2, 2, 2);
