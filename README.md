<p align="center">
  <img src="https://i.imgur.com/meePOPU.png" alt="RC_logo" width="100" />
</p>
<h1 align="center">Realtor Connect</h1>

### About

---

Realtor Connect is an app that helps connect realtors and home-seekers!

It is a tool that gives you, as a realtor, the ability to store your own properties, easily manage them, and share them with potential clients.
In turn, users can easily search for the properties they want and communicate with realtors.

### Documentation

---
The documentation is written in OpenApi and uses SwaggerUI. 
To read it, launch the project local and follow the link: [Documentation](http://localhost:8088).

### Quick start

---
Quick guide to run app.

1. Install Gradle. [Gradle install](https://gradle.org/install/).
2. Install PostgreSQL server. [Download PostgreSQL](https://www.postgresql.org/) and configure it.
3. If you use Docker, just run `docker run -d --name realtor-connect-db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=realtor-connect postgres:15.0`.
4. Clone the repository `git clone https://github.com/makurohashami/realtor-connect.git`.
5. Open bash/cmd in project directory.
6. Build the project.
7. Run app.
8. Well done! Now you can use [Application](http://localhost:8080)
