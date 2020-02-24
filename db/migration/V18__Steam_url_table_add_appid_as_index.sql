alter table t_steam_bundle_url add index idx_bundle_appid(appid);
alter table t_steam_demo_game_url add index idx_demo_game_appid(appid);
alter table t_steam_dlc_url add index idx_dlc_appid(appid);
alter table t_steam_game_url add index idx_game_appid(appid);
alter table t_steam_software_url add index idx_software_appid(appid);
alter table t_steam_sound_tape_url add index idx_sound_tape_appid(appid);
