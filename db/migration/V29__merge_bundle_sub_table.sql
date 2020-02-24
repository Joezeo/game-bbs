drop table t_steam_bundle_info;

rename table t_steam_sub_info to t_steam_sub_bundle_info;

alter table t_steam_sub_bundle_info comment 'steam 礼包、捆绑包信息';

alter table t_steam_sub_bundle_info
	add type varchar(10) not null comment '类型 - 捆绑包或礼包';

