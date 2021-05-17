DELETE FROM appusers;

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id)
VALUES(10, 0, 1, 'ROLE_ADMIN', 'admin','adminpass', 'admin@admin.com');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id)
VALUES(2, 0, 1, 'ROLE_TEACHER', 'teacher','teacherpass', 'teacher@teacher.com');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id)
VALUES(4, 0, 1, 'ROLE_USER', 'user','userpass', 'user@user.com');