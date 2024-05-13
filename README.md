# cooper-spring-chat

## application.properties 작성

    spring.application.name=chat
    server.port= 8080
    spring.thymeleaf.suffix=.html, .js
    logging.level.[org.springframework.web]=debug

    spring.data.mongodb.uri=mongodb+srv://Yun:tjrrbtjrrb13@cooper-chat.kku3wii.mongodb.net/?retryWrites=true&w=majority&appName=cooper-chat
    spring.jpa.hibernate.ddl-auto=update
    spring.data.mongodb.database=cooper
    logging.level.org.springframework=debug
    logging.level.org.springframework.web=debug
    
    eureka.client.register-with-eureka=true
    eureka.client.fetch-registry=true
    eureka.client.service-url.defaultZone=http://localhost:8761/eureka
    eureka.instance.instance-id=${spring.application.name}:${spring.application.instance_id:${random.value}}


    
