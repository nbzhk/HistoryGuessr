const {Map} = await google.maps.importLibrary("maps");
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

fetch("/result", {
    method: "POST"
})
    .then(response => response.json())
    .then(data => {
        console.log(data);
         currentRound = data.round;
        const actualCoordinates = new LatLng(data.pictureLocations[currentRound].latitude,
            data.pictureLocations[currentRound].longitude);

        new google.maps.Marker({
            position: actualCoordinates,
            map: map
        });

        //TODO: make round number update just before the round starts
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
    if (currentRound !== 5) {
        window.location.href = "/game";
    } else {
        window.location.href = "/summary"
    }
});


