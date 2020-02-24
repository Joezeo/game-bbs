alter table t_user modify id BIGINT auto_increment comment '主键id';
alter table t_question modify id BIGINT auto_increment comment '主键id';
alter table t_comment modify id BIGINT auto_increment comment '主键id';
alter table t_question modify userid BIGINT comment '提出问题的用户id';
alter table t_comment modify userid BIGINT comment '发出评论的用户id';
alter table t_comment modify parent_id BIGINT comment '该评论的父节点id';
