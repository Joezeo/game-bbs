create table t_steam_game_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '游戏url',
	constraint t_steam_game_url_pk
		primary key (id)
)
comment '存储steam所有游戏的url地址';

create table t_steam_bundle_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '捆绑包url',
	constraint t_steam_bundle_url_pk
		primary key (id)
)
comment '存储steam所有捆绑包的url地址';

create table t_steam_software_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '软件url',
	constraint t_steam_software_url_pk
		primary key (id)
)
comment 'steam所有软件url';

create table t_steam_dlc_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment 'DLC url',
	constraint t_steam_dlc_url_pk
		primary key (id)
)
comment 'steam所有DLC url';

create table t_steam_demo_game_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '试玩游戏url',
	constraint t_steam_demo_game_url_pk
		primary key (id)
)
comment 'steam所有试玩游戏url';

create table t_steam_sound_tape_url
(
	id int auto_increment comment '主键id，非steam app id',
	appid int not null comment 'app id',
	url varchar(255) not null comment '原声带url',
	constraint t_steam_sound_tape_url_pk
		primary key (id)
)
comment 'steam所有原声带url';
