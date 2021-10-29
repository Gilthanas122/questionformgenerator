# Question Form Generator

## Brief Introduction

This app was and is written to support a PhD research. 
It can generate questionforms with questions and users can fill out these questionforms.
A questionform can have four question types: radio button, check box, scale (it is a radio button question but with number from 1) and open-ended text question.
The main aim for creating this app was to create a functionality that most of the other questionform generators miss:
users are able here to vote for the answers provided by other users for the open-ended question. F.E.
Q: What are the best movies? The user can provide its answers and then see and vote for other users' answers for the same question. Besides this, the answers for the questionforms
are downloadable in an excel format and the creator of the question form can create queries for each and every question in 
the questionform separately.

## Functionality

### ADMIN

* is able to see, activate, deactivate, delete all users (but him/herself) in DB
* is able to add or take away roles from users
* compared to teacher role it can see all the questionforms created by the teachers

### TEACHER

* able to create, modify, delete questionform
* see all of this previous questionforms
* able to add, remove or modify order of the questions within his/her own questionform

### USER

* able to see available questionforms
* fill out questionforms and vote for other users' answers
* update previously filled out questionform and/or vote for answers provided by other users' after previously filling out questionform

## Installation Guide
* Download repo and set environment variables in application.properties (set SPRING_PROFILE for prod in case you do not wish to run the tests)
* Java 11+
* This application uses Java Mail Sender, in order for this to work, 
you have to set up a gmail account and connect it via environment variables (EMAIL_HOST, EMAIL_PORT, EMAIL_USERNAME, EMAIL_PASSWORD) to this app

## Some more info
* This app uses swagger, you can see all the endpoint at ../swagger-ui
* This app uses as template engine thymeleaf but is ready to receive a proper frontend as well and thus all the endpoints have a matching REST endpoint as well with the starting tag (rest/..)
* All of the functionalities has been tested (unit and integration tests for voting on other users' answers are still missing 29.10.2021)


