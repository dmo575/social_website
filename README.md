## WIP

### Backend
Own server (backend server)
Java + Spring Boot
Spring JPA for comms with DB
RESTful APIs for the front-end to interact with server

Contains front end files to send to client
Frontend will use bootstrap for UI

(Try to use calc server to run this too)

### Database
Own server (database server)
SQLite on another server

(Try to use portfolio server to run this too)

### Build tool
Maven

### Content
User can register
    Prio: WIth user and password
    Extra: Use emails for users, send out emails to confirm user email and register

User can log in with username and password

Users can post. Posts go HOME
Users can filter by tags (user can add tags to a post) when viewing HOME
Users can check out other user's profiles
Users can add others as friends
Users can accept or decline other users friends request

Users can go to FRIENDS
Users can select a friend in FRIENDS to open up a chat
Users can send and receive live chats

Users can delete posts they have made


### NOTES

TASKS:
Test bootstrap  DOING

Get a register page going. Make a submit form that sends a username and a password
    - Have a `GET /register` endpoint, returns PAGE. Check if the user is logged in, if so, then redirect to `/home`
    - PAGE will allow you to send a register form to `POST /register`
    - Whenever we unfocus the username field, send a `GET /user` to see if the user already exists. The return of this request checks a condition needed for the user to be able to send the registration form
    


### Commands:
Run the server (test) - `mvn spring-boot:run`



