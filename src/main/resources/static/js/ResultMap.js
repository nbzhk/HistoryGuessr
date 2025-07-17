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

const csrfToken = document.querySelector('meta[name="_csrf"]');
const token = csrfToken.getAttribute("content");

let inputUrl;
let currentRound;
const url = window.location.pathname;

const summaryRegex = /^\/summary\/round=(\d+)$/;
const summaryRound = url.match(summaryRegex);


const regex = /^\/profile\/best-game\/summary\/round=(\d+)$/;
const roundInfo = url.match(regex);



if (summaryRound) {
    currentRound = summaryRound[1] - 1;
}


if (isDaily) {
    inputUrl = "/daily/get-result"
} else if(roundInfo) {
    const round = roundInfo[1];
    inputUrl = `/summary/round-info/${round}`
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
            if (data.hasOwnProperty("round") && !roundInfo) {

                if (!summaryRound) {
                    currentRound = data.round - 1;
                }

                actualCoordinates = new LatLng(data.pictureLocations[currentRound].latitude,
                    data.pictureLocations[currentRound].longitude);

                guessCoordinates = new LatLng(data.userGuesses[currentRound].guessLat,
                    data.userGuesses[currentRound].guessLng);
            } else if (roundInfo) {
                actualCoordinates = new LatLng(data.pictureLocationDTO.latitude, data.pictureLocationDTO.longitude);

                guessCoordinates = new LatLng(data.userGuessDTO.guessLat,
                    data.userGuessDTO.guessLng)
            } else {
                actualCoordinates = new LatLng(data.picture.latitude, data.picture.longitude);

                guessCoordinates = new LatLng(data.userGuessDTO.guessLat,
                    data.userGuessDTO.guessLng)
            }

            new google.maps.Marker({
                position: actualCoordinates,
                map: map,
            });


            new google.maps.Marker({
                position: guessCoordinates,
                map: map,
                icon: "/images/GuessMarker (2).png"
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
