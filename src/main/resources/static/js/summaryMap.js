const {Map} = await google.maps.importLibrary("maps");
const {LatLng} = await google.maps.importLibrary("core");

const map = new Map(document.getElementById("summeryMap"), {
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

fetch("/summary", {
    method: "POST",
    headers: {
        'X-CSRF-TOKEN': token
    }
})
    .then(response => response.json())
    .then(data => {
        console.log(data);
        data.pictureLocations.forEach((location, index) => {
            const guess = data.userGuesses[index];
            setPictureLocationGuessPairMarker(location, guess);
        })
    });


function setPictureLocationGuessPairMarker(location, guess){
    const actualCoordinates = new LatLng(location.latitude, location.longitude);

    new google.maps.Marker({
        position: actualCoordinates,
        map: map
    });

    const guessCoordinates = new LatLng(guess.guessLat, guess.guessLng);
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

let newGameButton = document.getElementById("newGameButton");

newGameButton.addEventListener("click", () => {
    window.location.href = "/game";
});

let homeButton = document.getElementById("homeButton");

homeButton.addEventListener("click", () => {
    window.location.href = "/";
});


