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

// const actualLocationLatLng = new LatLng(48.858093, 2.294694);
// new google.maps.Marker({
//     position: actualLocationLatLng,
//     map: map
// });


fetch("/result", {
    method: "POST"
})
    .then(response => response.json())
    .then(data => {
        console.log(data);

        const actualCoordinates = new LatLng(data.pictureLocation.latitude, data.pictureLocation.longitude);

        new google.maps.Marker({
            position: actualCoordinates,
            map: map
        });


        if (data.userGuess.guessLat != null && data.userGuess.guessLng != null) {
            const guessCoordinates = new LatLng(data.userGuess.guessLat, data.userGuess.guessLng);
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


