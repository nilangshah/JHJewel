create database bill;
use bill;
create table user (username varchar(1000),password varchar(1000),password_salt varchar(1000));
insert into user values('admin','8450ECA01665516D9AEB5317764902B78495502637C96192C81B1683D32D691A0965CF037FECA8B9ED9EE6FC6AB8F27FCE8F77C4FD9B4A442A00FC317B8237E6','admin');