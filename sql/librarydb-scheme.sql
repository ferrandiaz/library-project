drop database if exists librarydb;
create database librarydb;
 
use librarydb;
 
create table users (
	username	varchar(20) not null primary key,
	userpass	char(32) not null,
	name		varchar(70) not null
);

create table user_roles (
	username			varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade,
	primary key (username, rolename)
);
 
create table books (
	bookid 			int not null auto_increment primary key,
	title 			varchar(20) not null,
	author			varchar(100) not null,
	lenguage		varchar(20) not null,
	ed_date			date,
	print_date		date,
	last_modified	timestamp,
	editorial		varchar(30) not null
);

create table review (

	reviewid 		int not null auto_increment,
	username		varchar(20) not null,
	bookid 			int not null,
	review			varchar(500) not null,
	name			varchar(20) not null,
	last_modified	timestamp,
	foreign key(username) references users(username),
	foreign key(bookid) references books(bookid),
	primary key (reviewid,username, bookid)
	);
	
