const messaging = firebase.messaging();

function requestPermission() {
  console.log('권한 요청 중...');
  Notification.requestPermission().then((permission) => {
    if (permission === 'granted') {
      console.log('알림 권한이 허용됨');
      getToken();
    } else {
      console.log('알림 권한 허용되지 않음');
    }
  });
}

function getToken() {
  messaging.getToken({ vapidKey: '' }).then((currentToken) => {
    if (currentToken) {
      console.log('FCM 토큰:', currentToken);
      const loginId = "1";
      sendTokenToServer(loginId, currentToken);
    } else {
      console.log('토큰을 얻을 수 없음');
    }
  }).catch((err) => {
    console.log('토큰 얻기 에러:', err);
  });
}

messaging.onMessage((payload) => {
  console.log('메시지 수신:', payload);
  // 여기서 알림을 표시하거나 다른 작업을 수행할 수 있습니다
});

// 페이지 로드 시 권한 요청
requestPermission();

function sendTokenToServer(loginId, token) {
  fetch('/fcm-messages/tokens', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ loginId: loginId, token: token }),
  })
  .then(response => response.json())
  .then(data => {
    console.log('Success:', data);
  })
  .catch((error) => {
    console.error('Error:', error);
  });
}