# LogProject

This repository houses the Log Project, divided into four modules.

- log-creator: Creator, a pure java project, aims to create data in a specific format.
- log-listener: Listener, which is a Spring project, is responsible for continuously producing the incoming data to Kafka "log-topic" by following the data coming from the log creator.
- log-consumer: Consumer, which is a Spring project, reads the data coming to "log-topic" using Kafka and segments the data and saves it to MongoDB. It also provides data to the "log-dashboard-app" via the created REST API.
- log-dashboard-app: Dashboard app, which is a React.js project, allows users to view the live log record graph.

Start the setup via cloning the repository in local machine:
```shell
git clone https://github.com/NilEmekci/log-project.git
```


## Run with docker-compose

The project comes with docker-compose.yml file. Through this file, the project can be installed and used on Docker, and all project dependencies such as kafka and mongo are automatically downloaded in the file.



### Important Checks

Before deploying any part of the Log Project, ensure you have the following requirements set up:

- **Port Allocation Check**
   - Project uses following ports, please ensure that these ports are not occupied by another application on both Docker and Local Computer. List of required ports:
     - Kafka Broker: 9092
     - MongoDB: 27017
     - Kafka-UI: 8080
     - Zookeeper: 2181
     - log-listener: 8082
     - log-consumer: 8081
     - log-dashboard app: 3000
     - mongo-express: 8084

- **Log File Path Update**
   - In docker-compose.yml of project, the following log file paths should be updated according to your computer. For this, the following steps must be performed. If you cloned the project, lines 31 and 46 in docker-compose.yml need to be updated:
       - "- C:\Users\Asus\IdeaProjects\log-project\logs:/app/logs" While the right side of ":" remains constant, the left side must be updated with the absolute path where your project's log file is located.




- **docker-compose up**
   - The path to which the project was cloned should be navigated through the terminal. After making sure that the project's directory is accessed, the following command is run to ensure that all modules work.
     ```shell
     docker-compose up
     ```
