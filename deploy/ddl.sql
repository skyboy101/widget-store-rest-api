create table inventory_product (id bigint generated by default as identity, version bigint not null, count integer not null, product_id bigint not null, primary key (id));
create table order_item (id bigint generated by default as identity, version bigint not null, purchase_order_id bigint not null, sku varchar(255) not null, total integer not null, primary key (id));
create table product (id bigint generated by default as identity, version bigint not null, product_info_id bigint, sku varchar(255) not null, primary key (id));
create table product_info (id bigint generated by default as identity, version bigint not null, description varchar(255) not null, primary key (id));
create table purchase_order (id bigint generated by default as identity, version bigint not null, primary key (id));
create table widget (id bigint not null, widget_finish_id bigint not null, widget_size_id bigint not null, widget_type_id bigint not null, primary key (id));
create table widget_finish (id bigint generated by default as identity, version bigint not null, name varchar(255) not null, sku_code varchar(255) not null, primary key (id));
create table widget_size (id bigint generated by default as identity, version bigint not null, name varchar(255) not null, sku_code varchar(255) not null, primary key (id));
create table widget_type (id bigint generated by default as identity, version bigint not null, name varchar(255) not null, sku_code varchar(255) not null, primary key (id));
alter table product add constraint UK_q1mafxn973ldq80m1irp3mpvq unique (sku);
alter table widget_finish add constraint UK_dwtsd3vihul50b26xluc4h4nc unique (name);
alter table widget_finish add constraint UK_5j0993elwgcx209xexje1wvb5 unique (sku_code);
alter table widget_size add constraint UK_8xx790ia07hdbq7qxt29y9otq unique (name);
alter table widget_size add constraint UK_iwm30pd0rsahc60hdfjbnxagh unique (sku_code);
alter table widget_type add constraint UK_2g4thfk0pa0d1dllsnpog9buj unique (name);
alter table widget_type add constraint UK_enbm152t0mky79a5xap9r04e2 unique (sku_code);
alter table inventory_product add constraint FK47vtq82lhhheejnn75sundyo6 foreign key (product_id) references product;
alter table order_item add constraint FKhmo3u0s8nh1tvnx551aq9dqsp foreign key (purchase_order_id) references purchase_order;
alter table product add constraint FKqnw4m5cthkq3wu09058sse7g6 foreign key (product_info_id) references product_info;
alter table widget add constraint FK64v3c8nrbwrp8t86koyr58yg7 foreign key (widget_finish_id) references widget_finish;
alter table widget add constraint FKo7wn2saye8d3gdfb7swgxf6pa foreign key (widget_size_id) references widget_size;
alter table widget add constraint FKsgq65b7mje27cfmwxp27ccvhn foreign key (widget_type_id) references widget_type;
