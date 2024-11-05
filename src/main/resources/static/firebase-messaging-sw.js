importScripts('https://www.gstatic.com/firebasejs/11.0.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/11.0.1/firebase-messaging-compat.js');
try {
    firebase.initializeApp({
        apiKey: "",
        authDomain: "",
        projectId: "",
        messagingSenderId: "",
        appId: "",
    });
} catch(err) {
    console.log(err);
}

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log('Received background message ', payload);
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: '/firebase-logo.png'
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener('push', function(event) {
    const options = {
        body: event.data.text(),
        icon: 'icon.png',
        badge: 'badge.png'
    };

    event.waitUntil(
        self.registration.showNotification('푸시 알림', options)
    );
});

self.addEventListener('notificationclick', function(event) {
    event.notification.close();
    // 알림 클릭 시 특정 URL로 이동
    event.waitUntil(
        clients.openWindow('https://google.com')
    );
});