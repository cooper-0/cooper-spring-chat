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
    loadRoomList();

    $("#messageForm").submit(function(event) {
        event.preventDefault();
        sendChat();
    });

    $("#deleteRoom").click(function() {
        if (roomId) {
            if (confirm(`현재 채팅방 ${roomId}을 삭제하시겠습니까?`)) {
                deleteRoom(roomId);
            }
        } else {
            alert('현재 접속 중인 채팅방이 없습니다.');
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
    var socket = new SockJS('http://121.155.7.164:8080/ws-stomp');
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

function loadRoomList() {
    fetch(`/api/room/list`)
        .then(response => response.json())
        .then(rooms => {
            if (rooms && rooms.length > 0) {
                rooms.forEach(room => {
                    newButton(room);
                });
            } else {
                console.log('찾을 수 없음');
            }
        })
        .catch(error => {
            console.error('채팅방 목록을 불러오는 중 오류가 발생했습니다:', error);
        });
}
function deleteRoom(roomId) {
    fetch(`/api/room/delete?roomId=${encodeURIComponent(roomId)}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('채팅방 삭제 실패');
            }
            removeButton(roomId);
        })
        .catch(error => {
            console.error('채팅방을 삭제하는 중 오류가 발생했습니다:', error);
        });
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

function getCollectionName(roomId) {
    switch (roomId) {
        default:
            return roomId; // 새로운 방의 경우 방 ID 자체를 컬렉션 이름으로 사용
    }
}

function loadChat(roomId) {
    const chatMessagesDiv = document.getElementById('chatting');
    const collectionName = getCollectionName(roomId);
    const url = `/api/chat/previous/${encodeURIComponent(collectionName)}`;

    fetch(url) // 서버의 API 엔드포인트로 요청을 보냅니다.
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답이 실패하였습니다.');
            }
            return response.json();
        })
        .then(messages => {
            chatMessagesDiv.innerHTML = ''; // 기존의 메시지를 지우고 새로운 메시지를 추가합니다.
            if (Array.isArray(messages)) {
                messages.forEach(message => {
                    const messageElement = document.createElement('div');
                    messageElement.textContent = `${message.senderID}: ${message.message}`;
                    // 삭제 버튼
                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = '삭제';
                    deleteButton.addEventListener('click', () => deleteMessage(message.id, collectionName));
                    messageElement.appendChild(deleteButton);
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
function newButton(roomId) {
    const newButton = document.createElement('button');
    newButton.className = 'room-option';
    newButton.dataset.room = roomId;
    newButton.textContent =  roomId;

    // 새로 생성된 채팅방 버튼에 클릭 이벤트 추가
    newButton.addEventListener('click', function () {
        selectRoom(roomId);

    });
    document.querySelector('.room-selection').appendChild(newButton);


}

function removeButton(roomId) {
    const roomButton = document.querySelector(`.room-option[data-room='${roomId}']`);
    if (roomButton) {
        roomButton.remove();
    }
}
// 동적으로 채팅방 생성
document.getElementById('createRoom').addEventListener('click', function () {
    const roomId = document.getElementById('newRoomId').value;
    if (roomId) {
        fetch('/api/room/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'roomId=' + encodeURIComponent(roomId)
        })
            .then(response => response.text())
    .then(data => {
            alert(data); // 채팅방 생성 성공 메시지
            newButton(roomId);
        })
    .catch(error => console.error('Error:', error));
    } else {
        alert('채팅방 이름을 입력하세요');
    }

});
// 메시지 삭제 버튼 엔드포인트
function deleteMessage(messageId, collectionName) {
    fetch(`/api/chat/delete?messageId=${messageId}&collectionName=${encodeURIComponent(collectionName)}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('메시지 삭제 실패');
            }
            // 메시지 삭제 후 채팅 메시지를 다시 로드
            loadChat(roomId);
        })
        .catch(error => {
            console.error('메시지를 삭제하는 중 오류가 발생했습니다:', error);
        });
}
// 방 선택 함수
function selectRoom(room) {
    roomId = room;
    console.log("Room selected:", roomId);

    // 방이 변경될 때마다 해당 방의 채팅을 불러오도록 loadChat 함수 호출

    // 선택한 채팅방 정보를 서버로 전송
    if (stompClient && stompClient.connected) {
        stompClient.send("/selectRoom", {}, JSON.stringify({
            'roomId': roomId
        }));

        // 기존 구독 취소하고 새로운 방에 대해 구독
        stompClient.unsubscribe('/room/' + roomId);
        stompClient.subscribe('/room/' + roomId, function (chatMessageDTO) {
            console.log("New message received:", chatMessageDTO);
        });
    }
    loadChat(roomId);
}

function showChat(chat) {
    var messageContent = "[" + chat.senderID + "] " + chat.message;

    $("#chatting").append(
        "<div class='chatting'><tr><td>" + messageContent + "</td></tr></div>"
    );
    $('#chatting').scrollTop($('#chatting')[0].scrollHeight);
}

$(".room-option").click(function() {
    var room = $(this).data("room");
    selectRoom(room); // 방 선택 시 해당 방 정보를 서버로 전송
});


$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault(); // 기본 동작 방지
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

