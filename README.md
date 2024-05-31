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


## WebSocket 및 STOMP 구성

● STOMP 엔드포인트: '/cooper-chat'

● 메시지 브로커
- '/app' 경로로 메시지를 전송
- '/topic' 경로로 메시지 수신


## REST API 구성

### 1. @PostMapping("/message")

● URL: '/cooper-chat/message'
- 요청 방식: POST
- 클라이언트가 채팅 메시지를 전송할 때 호출
- 메시지를 'collectionName'이라는 컬렉션에 저장

### 2. @GetMapping("/previous/{collectionName}")

● URL: '/cooper-chat/previous/{collectionName}'
● 요청 방식: GET
● 특정 컬렉션의 이전 채팅 메시지를 반환

  응답: 'List<Chat>' 객체로 반환

### 3. @DeleteMapping("/deleteChat")

● URL: '/cooper-chat/deleteChat'
