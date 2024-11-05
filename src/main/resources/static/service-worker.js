const sCacheName = "my-pwa";
const aFilesToCache = ['./', './index.html', './manifest.json', './badge.png']

self.addEventListener('install', pEvent => {
    console.log('install service-worker');
    pEvent.waitUntil(
        caches.open(sCacheName)
        .then(pCache => {
            console.log('save File to Cache');
            return pCache.addAll(aFilesToCache);
        })
    )
});

self.addEventListener('active', pEvent => {
    console.log('start service-worker');
});

self.addEventListener('fetch', pEvent => {
    pEvent.respondWith(
        caches.match(pEvent.request)
        .then(response => {
            if (!response) {
                console.log('request date from network', pEvent.request)
                return fetch(pEvent.request);
            }
            console.log('request data from cache', pEvent.request)
            return response;
        }).catch(err => console.log(err))
    );
});