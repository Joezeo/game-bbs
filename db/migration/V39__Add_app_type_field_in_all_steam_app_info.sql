alter table t_steam_game_info
	add app_type int default 1 not null comment '用于作为solr字段的属性，应用类型 1-game';

alter table t_steam_software_info
	add app_type int default 2 not null comment '用于作为solr字段的属性，应用类型 2-software';

alter table t_steam_dlc_info
	add app_type int default 3 not null comment '用于作为solr字段的属性，应用类型 3-dlc';

alter table t_steam_demo_game_info
	add app_type int default 4 not null comment '用于作为solr字段的属性，应用类型 4-demo game';

alter table t_steam_sound_tape_info
	add app_type int default 6 not null comment '用于作为solr字段的属性，应用类型 6-sound tape';

alter table t_steam_sub_bundle_info
	add app_type int not null comment '用于作为solr字段的属性，应用类型 7-sub 5-bundle';

update t_steam_sub_bundle_info set app_type = 5 where `type`='bundle';
update t_steam_sub_bundle_info set app_type = 7 where `type`='sub';
