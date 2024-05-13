var stompClient = null;
var roomId = "{room}";
var chat = "[]"; // 초기 채팅 리스트
var senderID = "";
var senderEmail = "Yun@example.com";

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
    var socket = new SockJS('http://192.168.0.17:8080/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected:', frame); // 연결 확인 로그


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
window.onload = function() {
    loadChat();
};

function loadChat() {
    fetch('/api/chat/previous') // 서버의 API 엔드포인트로 요청을 보냅니다.
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답이 실패하였습니다.');
            }
            return response.json();
        })
        .then(messages => {
            const chatMessagesDiv = document.getElementById('chatting');
            chatMessagesDiv.innerHTML = ''; // 기존의 메시지를 지우고 새로운 메시지를 추가합니다.
            if (Array.isArray(messages)) {
                messages.forEach(message => {
                    const messageElement = document.createElement('div');
                    messageElement.textContent = `${message.senderID}: ${message.message}`;
                    chatMessagesDiv.appendChild(messageElement);
                });
            } else {
                console.error('서버 응답 형식이 올바르지 않습니다.');
            }
        })
        .catch(error => {
            console.error('채팅 메시지를 불러오는 중 오류가 발생했습니다:', error);
        });
}



// 보낸 채팅 보기

function showChat(chat) {
    var messageContent = "[" + chat.senderID + "] " + chat.message;

        $("#chatting").append(
            "<div class='chatting'><tr><td>" + messageContent + "</td></tr></div>"
        );

        /*
        $("#chatting").append(
            "<div class='chatting'><tr><td>" + messageContent + "</td></tr></div>"
        );

         */


    // 스크롤을 맨 아래로 이동
    $('#chatting').scrollTop($('#chatting')[0].scrollHeight);
}

// 방 선택 함수
function selectRoom(room) {
    roomId = room;
    console.log("Room selected:", roomId);
}

// 방 선택 이벤트 처리
$(".room-option").click(function() {
    var room = $(this).data("room");
    selectRoom(room);
});


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
    $("#logout").click(function() {
        senderID = "";
        $(".userModal").css("display", "block"); // 사용자 이름 설정 모달 보이기
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
