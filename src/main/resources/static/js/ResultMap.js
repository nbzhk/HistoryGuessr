const {Map} = await google.maps.importLibrary("maps");
console.log("importLibrary MAPS")

const {LatLng} = await google.maps.importLibrary("core");

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

fetch("/game/get-result", {
    method: "POST",
    headers: {
        'X-CSRF-TOKEN': token
    }

})
    .then(response => response.json())
    .then(data => {
        console.log(data);
         currentRound = data.round;
        const actualCoordinates = new LatLng(data.pictureLocations[currentRound - 1].latitude,
            data.pictureLocations[currentRound - 1].longitude);

        new google.maps.Marker({
            position: actualCoordinates,
            map: map
        });

        if (data.userGuesses[currentRound - 1].guessLat != null && data.userGuesses[currentRound - 1].guessLng != null) {
            const guessCoordinates = new LatLng(data.userGuesses[currentRound - 1].guessLat, data.userGuesses[currentRound - 1].guessLng);
            new google.maps.Marker({
                position: guessCoordinates,
                map: map
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
    });


const nextButton = document.getElementById("nextButton");

nextButton.addEventListener("click", () => {
    if (nextButton.value === 'Next Round') {
        window.location.href = "/game";
    } else {
        window.location.href = "/summary"
    }
});


