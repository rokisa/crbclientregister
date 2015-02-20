# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table client_details (
  client_id                 bigint auto_increment not null,
  national_id               varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  email_address             varchar(255),
  phone_number              varchar(255),
  nationality               varchar(255),
  physical_address          varchar(255),
  date_of_birth             bigint,
  occupation                varchar(255),
  national_id_photo         varchar(255),
  photo                     varchar(255),
  constraint pk_client_details primary key (client_id))
;

create table next_of_kin (
  next_of_kin_id            bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  relationship              varchar(255),
  phone_number              varchar(255),
  email_address             varchar(255),
  national_id               varchar(255),
  clientDetails             bigint,
  constraint pk_next_of_kin primary key (next_of_kin_id))
;

alter table next_of_kin add constraint fk_next_of_kin_clientDetails_1 foreign key (clientDetails) references client_details (client_id) on delete restrict on update restrict;
create index ix_next_of_kin_clientDetails_1 on next_of_kin (clientDetails);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table client_details;

drop table next_of_kin;

SET FOREIGN_KEY_CHECKS=1;

