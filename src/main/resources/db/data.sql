DELETE FROM appusers;

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(1, 0, 1, 'ROLE_ADMIN', 'admin','adminpass', 'admin@admin.com', 'admin-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(2, 0, 1, 'ROLE_TEACHER', 'teacher','teacherpass', 'teacher@teacher.com', 'teacher-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(3, 0, 1, 'ROLE_USER', 'user','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'user@user.com', 'user-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(4, 0, 0, 'ROLE_USER', 'usernotactivated','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'usernotactivated@user.com', 'user-not-activated-token');