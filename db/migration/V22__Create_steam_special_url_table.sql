create table t_steam_special_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '游戏url',
	constraint t_steam_game_url_pk
		primary key (id)
)
comment '存储steam所有特惠商品的url地址';

alter table t_steam_special_url add index idx_special_appid(appid);