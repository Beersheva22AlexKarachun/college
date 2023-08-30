delete from marks;
delete from subjects;
delete from students_lecturers;

insert into students_lecturers (id, name, birth_date, dtype) values(123,'Jerry', '2000-10-10', 'Student');
insert into students_lecturers (id, name, birth_date, dtype) values(124,'Summer', '2004-10-10', 'Student');
insert into students_lecturers (id, name, birth_date, dtype) values(125,'Poopybutthole', '1994-10-10', 'Student');
insert into students_lecturers (id, name, birth_date, dtype) values(126,'Birdperson', '1997-10-10', 'Student');
insert into students_lecturers (id, name, birth_date, dtype) values(127,'Beth', '2001-10-10', 'Student');
insert into students_lecturers (id, name, birth_date, dtype) values(321,'Morty Smith', '2009-10-10', 'Lecturer');
insert into students_lecturers (id, name, birth_date, dtype) values(421,'Rick Sanchez', '1953-10-10', 'Lecturer');

insert into subjects (id, name, hours, type, lecturer_id) values('J1', 'Java Core', 100, 'BACK_END', 321);
insert into subjects (id, name, hours, type, lecturer_id) values('J2', 'Java Technologies', 100, 'BACK_END', 321);
insert into subjects (id, name, hours, type, lecturer_id) values('JS1', 'JS', 100, 'FRONT_END', 421);
insert into subjects (id, name, hours, type, lecturer_id) values('JS2', 'React', 100, 'FRONT_END', 421);

insert into marks (student_id, subject_id, mark) values(123, 'J1', 80);
insert into marks (student_id, subject_id, mark) values(123, 'J2', 100);
insert into marks (student_id, subject_id, mark) values(123, 'JS1', 95);
insert into marks (student_id, subject_id, mark) values(123, 'JS2', 90);

insert into marks (student_id, subject_id, mark) values(127, 'J1', 100);
insert into marks (student_id, subject_id, mark) values(127, 'J2', 100);
insert into marks (student_id, subject_id, mark) values(127, 'JS1', 100);
insert into marks (student_id, subject_id, mark) values(127, 'JS2', 100);

insert into marks (student_id, subject_id, mark) values(124, 'J1', 80);
insert into marks (student_id, subject_id, mark) values(124, 'J2', 80);
insert into marks (student_id, subject_id, mark) values(124, 'JS1', 80);
insert into marks (student_id, subject_id, mark) values(124, 'JS2', 80);

insert into marks (student_id, subject_id, mark) values(125, 'J2', 80);
insert into marks (student_id, subject_id, mark) values(125, 'JS1', 80);


