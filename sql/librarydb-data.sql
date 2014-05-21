source librarydb-schema.sql;

insert into users values('admin', MD5('admin'), 'admin');
insert into user_roles values ('admin', 'admin');

insert into users values('test', MD5('test'), 'Test');
insert into user_roles values ('test', 'registered');

insert into users values('test2', MD5('test2'), 'Test2');
insert into user_roles values ('test2', 'registered');



insert into books (title, author, lenguage, ed_date, print_date, editorial, last_modified) values ('Book1', 'Author1', 'English', '2000-10-5', '2000-7-6', 'Editorial1', now()); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book2', 'Author1', 'Catalan', '2001-11-3', '2001-8-10', 'Editorial1'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book3', 'Author2', 'Spanish', '2003-2-3', '2003-7-6', 'Editorial1'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book4', 'Author2', 'French', '2005-4-5', '2005-8-10', 'Editorial1'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book5', 'Author2', 'English', '2007-5-5', '2007-7-6', 'Editorial1'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book6', 'Author3', 'Swahili', '2000-6-5', '2000-10-6', 'Editorial2'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book7', 'Author4', 'English', '2001-8-8', '2008-12-6', 'Editorial2'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book8', 'Author4', 'English', '2010-1-8', '2010-7-6', 'Editorial2'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book9', 'Author4', 'English', '2012-7-5', '2012-8-6', 'Editorial2'); 
insert into books (title, author, lenguage, ed_date, print_date, editorial) values ('Book10', 'Author5', 'English', '2005-10-5', '2005-7-6', 'Editorial2'); 

insert into review (username, bookid, review, name) values( 'test', '1', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test2', '1', 'review1', 'Test2');
insert into review (username, bookid, review, name) values('test', '2', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test2', '2', 'review1', 'Test2');
insert into review (username, bookid, review, name) values('test', '3', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '4', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '5', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '6', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '7', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '8', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '9', 'review1', 'Test');
insert into review (username, bookid, review, name) values('test', '10', 'review1', 'Test');



