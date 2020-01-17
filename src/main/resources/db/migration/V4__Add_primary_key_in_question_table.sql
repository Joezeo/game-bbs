alter table t_question modify id int auto_increment comment '主键id';

alter table t_question
	add constraint t_question_pk
		primary key (id);
