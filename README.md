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

● 요청 방식: POST

  

      "senderID: "user",
  
      "senderEmail": user@example.com",
  
      "message": "Hello",
  
      "roomId": "testroom"
  
  

● 응답:

 
 
    "message": "채팅 메시지가 성공적으로 저장되었습니다."
    
 


### 2. @GetMapping("/previous/{collectionName}")

● URL: '/cooper-chat/previous/{collectionName}'

● 요청 방식: GET

● 특정 컬렉션의 이전 채팅 메시지를 반환

● 응답: 'List<Chat>' 객체로 반환


    
    "_id":{"$oid":"665819bcbff7357f149bad52"},

    "senderID":"User",
    
    "senderEmail":"user@example.com",
    
    "message":"ㅇㅇ",
    
    "sendDate":{"$date":{"$numberLong":"1717049788962"}},
    
    "_class":"com.cooper.chat.chat.model.Chat"


    
  

### 3. @DeleteMapping("/deleteChat")

● URL: '/cooper-chat/deleteChat'

● 요청 방식: DELETE

● 요청 파라미터:

- 'messageId', 'collectionName'

- '/cooper-chat/deleteChat?messageId=${messageId}&collectionName=${encodeURIComponent(collectionName)}'



### 4. @PostMapping("/create")

● URL: '/cooper-chat/create'

● 요청 방식: POST



    "roomId": "testroom"



● 응답



    "message": "채팅방이 생성되었습니다."




### 5. @DeleteMapping("/deleteRoom")

● URL: '/cooper-chat/deleteRoom'

● 요청 방식: DELETE

● 요청 파라미터: 'rooId'

- /cooper-chat/deleteRoom?roomId=testroom


##
