# Zlagoda AIS - Retail Management System

## 📝 Overview
**Zlagoda AIS** is an automated information system designed for managing retail store operations. The system allows for efficient tracking of employees, inventory, products, and sales records. It provides a web-based interface for administrators and cashiers to manage the store's daily workflow.

## 👥 Authors & Collaboration
This project was developed as a **group assignment** by three students from the **National University of Kyiv-Mohyla Academy (NaUKMA). 

## 🛠 Tech Stack
* **Language:** Java (Servlets & JSP)
* **Build Tool:** Maven
* **Database:** SQLite
* **Server:** Apache Tomcat 9.0.89
* **Architecture:** DAO (Data Access Object) pattern

## 🚀 Getting Started

### 1. Database Configuration (Crucial)
The project uses an SQLite database. Due to the environment specifics, the database file must be placed correctly for the server to access it:
1. Locate the `zlagodaa.db` file in `src/main/resources/`.
2. Copy this file into your Tomcat installation directory: `{TOMCAT_HOME}/bin/resources/zlagodaa.db`.
   * *Note: The system looks for the database at the relative path `resources/zlagodaa.db` from the server's working directory.*

### 2. Build the Project
Use Maven to compile and package the application:
```bash
mvn clean install

Deployment
Open your IDE (e.g., IntelliJ IDEA).

Configure Tomcat Server 9.0.89.

In the Deployment tab, add the zlagoda_ais:war exploded artifact.

Set the Application Context to /.

Run the server.

🔐 Default Credentials
To access the system, use the following administrator account:
Username: VasAd
Password: 1
