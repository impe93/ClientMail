# ClientMail

I and my team made this project for a University of Turin course. The aim of the project was to create an Email client and
a Server that can handle the client requests.

The client GUI is made with the Java Swing library and the communication between the server and the client by
using RMI (Remote Method Invocation). The emails are saved in an SQLite database and JDBC is used for the connection
between DB and client/server.

By starting the server and one, two or three clients is
possible to send, receive, delete the email, check if there are new ones. By starting just the client, without
the server is still possible to check the mailbox, but is impossible to receive, delete or send new emails. 

## Getting Started

### Prerequisites

We used NetBeans as IDE for developing the application and we suggest to use that for testing the application too.

### Installing

Before start using the application is important to set the right email for the client and initialize the databases of the three clients and the one for the server.

First of all, run InizializzazioneDBClient.java in the client package and InizializzazioneDBServer.java in the server package. This will create an initial database for the application.

Secondly is important to launch the server before the clients start because clients don't handle reconnection to the server.

After that is important to set the email as an argument of the running properties. Is possible to use 3 different emails:

* lorenzo.imperatrice@edu.unito.it
* francesca.riddone@edu.unito.it
* alessio.berger@edu.unito.it

If you would like to run all three clients at the same time, you'll have to reset every time the argument and change it to another email of the list.
For the server there is no need to set the email or any run configuration.

## Contributing

The three member that made this project are:

* Lorenzo Imperatrice: Client GUI and client controller.
* Alessio Berger: Server GUI, Server controller, server model.
* Francesca Riddone: Client Model

## License

This project is licensed under the MIT License
