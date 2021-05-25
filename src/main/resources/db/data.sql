INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(1, 0, 1, 'ROLE_ADMIN, ROLE_TEACHER', 'admin','$2a$10$KkPGU7EVwA/ug4.9K/iCA.0vdj4xW2h1hJ0dEvHF8mmyy45foW0AK', 'admin@admin.com', 'admin-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(2, 0, 1, 'ROLE_USER,ROLE_TEACHER', 'teacher','teacherpass', 'teacher@teacher.com', 'teacher-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(3, 0, 1, 'ROLE_USER', 'user','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'user@user.com', 'user-token');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(5, 0, 1, 'ROLE_USER,ROLE_TEACHER', 'teacher2','teacherpass2', 'teacher2@teacher.com', 'teacher-token2');

INSERT INTO appusers (id, disabled, active, roles, username, password, email_id, confirmation_token)
VALUES(4, 0, 0, 'ROLE_USER', 'usernotactivated','$2a$10$1k91J0vUcNZRIOfURLs35O1YlDsEeCAHjiiiddyh0mrmaetXykfWi', 'usernotactivated@user.com', 'user-not-activated-token');

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(1, 0, 'test questionform description', 0, 'test questionform', 2);

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(2, 0, 'test questionform description finished', 1, 'test questionform finished', 2);

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(3, 0, 'test questionform for another teacher', 1, 'test questionform belonging to another user', 1);

INSERT INTO questionforms (id, deleted, description, finished, name, app_user_id)
VALUES(4, 0, 'test questionform with missing user', 1, 'test questionform with missing user', null);

INSERT INTO questions (question_type, id, deleted, list_position, scale, question_form_id)
VALUES('CheckBoxQuestion', 1, 0, 0, 0, 1);

INSERT INTO questions (question_type, id, deleted, list_position, scale, question_form_id)
VALUES('RadioButtonQuestion', 2, 0, 1, 0, 1);

INSERT INTO questions (question_type, id, deleted, list_position, scale, question_form_id)
VALUES('ScaleQuestion', 3, 0, 2, 5, 1);

INSERT INTO questions (question_type, id, deleted, list_position, scale, question_form_id)
VALUES('TextQuestion', 4, 0, 3, 0, 1);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (1, 0, 'Radio', 2);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (2, 0, 'Radio 2', 2);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (3, 0, 'Radio 3', 2);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (4, 0, 'Check box', 1);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (5, 0, 'Check box 2', 1);

INSERT INTO questiontextpossibilities(id, deleted, answer_text, multiple_answer_question_id)
VALUES (6, 0, 'Check box 3', 1);
