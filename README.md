# cooper-spring-chat

## application.properties 작성

    spring.application.name=chat
    #포트번호 (랜덤포트는 0으로)
    server.port= 8080
    spring.thymeleaf.suffix=.html, .js
    logging.level.[org.springframework.web]=debug
    
    #몽고DB 연결
    spring.data.mongodb.uri=mongodb+srv://user:<password><Cluster name>.kku3wii.mongodb.net/?retryWrites=true&w=majority&appName=<Cluster name>
    spring.data.mongodb.database=cooper
    
    spring.jpa.hibernate.ddl-auto=update
    logging.level.org.springframework=debug
    logging.level.org.springframework.web=debug
    
    #스프링부트 클라우드 유레카
    eureka.client.register-with-eureka=true
    eureka.client.fetch-registry=true
    eureka.client.service-url.defaultZone=http://localhost:8761/eureka
    eureka.instance.instance-id=${spring.application.name}:${spring.application.instance_id:${random.value}}
        
