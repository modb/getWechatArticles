# wechatNews
get wechat news based on wechat api


# Installation
create table on postgresql

```sql
create table wechat_news
(
	id integer not null	primary key,
	title varchar(128) not null,
	created_time timestamp(0),
	source varchar(64),
	type smallint,
	url varchar(512),
	image_id varchar(512),	
	image_url varchar(512),
	brief varchar(256) not null,
);

CREATE UNIQUE INDEX wechat_news_image_id_uindex ON wechat_news (image_id);

CREATE SEQUENCE seq_wechat_news INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

```

# RUN

```shell
java -jar xxx.jar appId appSecret offset count type
```

| 名称      | 必须     | 说明     |
| ---------- | :-----------:  | :-----------: |
| appId     | 是    | 微信公众号ID    |
| appSecret     | 是    | 微信公众号秘钥    |
| offset     | 是    | 从第几个开始    |
| count     | 是    | 获取多少个    |
| type     | 是    | 类型：2-公众号    |