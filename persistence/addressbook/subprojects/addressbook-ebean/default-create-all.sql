create table contacts (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  lastname                      varchar(255) not null,
  address                       varchar(255) not null,
  company                       varchar(255) not null,
  email                         varchar(255) not null,
  constraint pk_contacts primary key (id)
);

