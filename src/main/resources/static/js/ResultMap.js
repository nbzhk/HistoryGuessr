const {Map} = await google.maps.importLibrary("maps");


const {LatLng} = await google.maps.importLibrary("core");

const isDaily = document.body.classList.contains("daily-challenge");

const map = new Map(document.getElementById("resultMap"), {
    zoom: 0,
    center: {lat: 0, lng: 0},
    mapTypeControl: false,
    fullscreenControl: false,
    streetViewControl: false,
    minZoom: 1,
    restriction: {
        latLngBounds: {
            north: 85,
            south: -85,
            west: -180,
            east: 180,
        }
    },
});

let currentRound;

const csrfToken = document.querySelector('meta[name="_csrf"]');
const token = csrfToken.getAttribute("content");

let inputUrl;

if (isDaily) {
    inputUrl = "/daily/get-result"
} else {
    inputUrl = "/game/get-result"
}

fetch(inputUrl, {
    method: "POST",
    headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': token
    }

})
    .then(response => response.json())
    .then(data => {
            let actualCoordinates;
            let guessCoordinates;

            if (data.hasOwnProperty("round")) {
                currentRound = data.round - 1;
                actualCoordinates = new LatLng(data.pictureLocations[currentRound].latitude,
                    data.pictureLocations[currentRound].longitude);

                guessCoordinates = new LatLng(data.userGuesses[currentRound].guessLat,
                    data.userGuesses[currentRound].guessLng);
            } else {
                actualCoordinates = new LatLng(data.picture.latitude, data.picture.longitude);

                guessCoordinates = new LatLng(data.userGuessDTO.guessLat,
                    data.userGuessDTO.guessLng)
            }

            new google.maps.Marker({
                position: actualCoordinates,
                map: map
            });


            new google.maps.Marker({
                position: guessCoordinates,
                map: map,
                icon: "/images/GuessMarker.png", scaledSize: new google.maps.Size(50, 50),
            });

            let lineCoordinates = [
                actualCoordinates,
                guessCoordinates
            ];

            const lineSymbol = {
                path: "M 0,-1 0,1",
                strokeOpacity: 1,
                scale: 2,
            };

            let linePath = new google.maps.Polyline({
                path: lineCoordinates,
                strokeOpacity: 0,
                icons: [{
                    icon: lineSymbol,
                    offset: "0",
                    repeat: "10px"
                }]
            });

            linePath.setMap(map);

        }
    );
