create table t_steam_history_price
(
	id BIGINT auto_increment comment '主键id',
	appid int not null comment 'steam app id',
	price int not null comment '价格',
	gmt_create BIGINT not null comment '记录该价格的时间，gmt-格林威治中央区时',
	constraint t_steam_history_price_pk
		primary key (id)
)
comment 'steam中曾经进入优惠的产品的历史价格表，该表记录下的价格不允许修改';

alter table t_steam_history_price add index idx_history_price_appid(appid);