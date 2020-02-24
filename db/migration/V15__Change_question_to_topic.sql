alter table t_question modify title varchar(100) null comment '帖子标题';

alter table t_question modify description varchar(256) null comment '帖子描述';

alter table t_question modify tag varchar(256) null comment '帖子标签，以英文逗号作为分割';

alter table t_question modify comment_count int null comment '帖子的评论数';

alter table t_question modify view_count int null comment '帖子浏览数';

alter table t_question modify like_count int null comment '帖子点赞数';

alter table t_question modify gmt_create bigint null comment '帖子创建时间';

alter table t_question modify gmt_modify bigint null comment '帖子修改时间';

alter table t_question modify userid bigint null comment '创作帖子的用户id';

rename table t_question to t_topic;

alter table t_topic comment '存储帖子信息';

alter table t_topic
	add topic_type int not null comment '帖子的分类 1-问题 2-技术 3-创意 4-好玩';