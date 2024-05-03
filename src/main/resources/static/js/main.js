$(document).ready(function(){
    // 페이지 로드 시 이름 설정 모달 표시
    $(".userModal").css("display", "block");

    // 모달 닫기 버튼 클릭 이벤트
    $(".close-button").click(function(){
        $("#modal-background").css("display", "none");
        $(".userModal").css("display", "none"); // 모달 숨기기
    });

    // 이름 설정 버튼 클릭 시
    $("#setUsername").click(function(){
        var username = $("#username").val(); // 사용자 이름 가져오기

        // 사용자 이름이 유효한지 확인
        if(username.trim() !== "") {
            // 사용자 이름을 서버로 전송
            stompClient.send("/setUsername", {}, JSON.stringify({
                'username': username
            }));

            // 모달 닫기
            $(".userModal").css("display", "none");
            $("#modal-background").css("display", "none");
            // 사용자 이름을 전역 변수에 저장
            senderID = username;
            // 사용자 이름을 서버로 보내거나 다른 작업 수행
            console.log("사용자 이름:", username);
        } else {
            alert("이름을 입력하세요.");
        }
    });
});

var stompClient = null;
var roomId = "123";
var chatList = "[]"; // 초기 채팅 리스트
var senderID = "";
var senderEmail = "Yun@example.com";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#chatting").html(""); // 연결 상태에 따라 초기화
}

function connect() {
    var socket = new SockJS('http://172.16.82.131:8080/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected:', frame); // 연결 확인 로그
        loadChat(chatList); // 저장된 채팅 불러오기

        // 구독
        stompClient.subscribe('/room/' + roomId, function (chatMessageDTO) {
            console.log("New message received:", chatMessageDTO); // 메시지 수신 확인
            showChat(JSON.parse(chatMessageDTO.body)); // 수신된 메시지로 채팅 표시
        });
        // 서버에서 사용자 이름 수신
        stompClient.subscribe('/username', function (username) {
            console.log("Username received:", username); // 사용자 이름 수신 확인
            senderID = JSON.parse(username.body).username; // 전역 변수에 저장
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected"); // 연결 해제 로그
}

function sendChat() {
    var message = $("#message").val(); // 입력 필드의 메시지 가져오기

    if (message !== "") {
        stompClient.send("/send/" + roomId, {},
            JSON.stringify({
                'senderID': senderID,
                'senderEmail': senderEmail,
                'message': message
            })
        );

        $("#message").val(''); // 전송 후 입력 필드 비우기
    }
}

// 저장된 채팅 불러오기
function loadChat(chatListString) {
    try {
        var chatList = JSON.parse(chatListString); // JSON 파싱

        if (Array.isArray(chatList) && chatList.length > 0) {
            chatList.forEach(function(chat) {
                showChat(chat); // 각 채팅 표시
            });
        } else {
            console.error("Empty chat list or invalid format:", chatList); // 빈 배열 또는 형식 오류
        }
    } catch (error) {
        console.error("Error parsing chat list:", error); // 파싱 오류
    }
}

// 보낸 채팅 보기
function showChat(chatMessageDTO) {
    var messageContent = "[" + chatMessageDTO.senderID + "] " + chatMessageDTO.message;
    if (chatMessageDTO.senderEmail === senderEmail) {
        $("#chatting").append(
            "<div class='chatting_own'><tr><td>" + messageContent + "</td></tr></div>"
        );
    } else {
        $("#chatting").append(
            "<div class='chatting'><tr><td>" + messageContent + "</td></tr></div>"
        );
    }

    // 스크롤을 맨 아래로 이동
    $('.col-md-12').scrollTop($('.col-md-12')[0].scrollHeight);
}

// 이벤트 핸들링
$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault(); // 기본 동작 방지
    });

    $("#connect").click(function() {
        connect(); // 연결
    });

    $("#disconnect").click(function() {
        disconnect(); // 연결 해제
    });

    $("#send").click(function() {
        sendChat(); // 채팅 전송
    });
});

// 페이지 로드 시 자동 연결
$(document).ready(function() {
    connect();
});

// 페이지 닫을 때 자동 연결 해제
$(window).on("beforeunload", function() {
    disconnect();
});

$(document).ready(function() {
    // 모달 닫기 버튼 클릭 이벤트 핸들링
    $(".close-button").click(function() {
        $(".modal").hide(); // 모달 숨기기
    });
});

// 모달 토글
var modal = document.querySelector(".modal");
var trigger = document.getElementById("send");
var closeButton = document.querySelector(".close-button");

function toggleModal() {
    modal.classList.toggle("show-modal"); // 모달 토글
}

trigger.addEventListener("click", toggleModal);
closeButton.addEventListener("click", toggleModal);
