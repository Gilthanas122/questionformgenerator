INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(1, 0, 1, 'ROLE_ADMIN', 'admin','$2a$10$KkPGU7EVwA/ug4.9K/iCA.0vdj4xW2h1hJ0dEvHF8mmyy45foW0AK', 'admin@admin.com', 'admin-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(2, 0, 1, 'ROLE_USER,ROLE_TEACHER', 'teacher','teacherpass', 'teacher@teacher.com', 'teacher-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(3, 0, 1, 'ROLE_USER', 'user','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'user@user.com', 'user-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(4, 0, 0, 'ROLE_USER', 'usernotactivated','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'usernotactivated@user.com', 'user-not-activated-token');

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(1, 0, 'test questionform description', 0, 'test questionform', 2);

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(2, 0, 'test questionform description finished', 1, 'test questionform finished', 3);