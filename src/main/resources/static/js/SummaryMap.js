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

let inputUrl;
const url = window.location.pathname;
const regex = /^\/profile\/best-game\/summary\/(\d+)$/;

let fromProfile = false;
let fromSummary = false;

if (url.match(regex)) {
    inputUrl = "/profile/best";
    fromProfile = true;
} else {
    inputUrl = "/game/summary";
    fromSummary = true;
}

let guess;

fetch(inputUrl, {
    method: "POST",
    headers: {
        'X-CSRF-TOKEN': token
    }
})
    .then(response => response.json())
    .then(data => {
        data.pictureLocations.forEach((location, index) => {
            guess = data.userGuesses[index];
            setPictureLocationGuessPairMarker(location, guess);
        })
    });


function setPictureLocationGuessPairMarker(location, guess) {
    const actualCoordinates = new LatLng(location.latitude, location.longitude);

    new google.maps.Marker({
        position: actualCoordinates,
        map: map,
    });

    const guessCoordinates = new LatLng(guess.guessLat, guess.guessLng);
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


const rounds = document.querySelectorAll('.round-info');

rounds.forEach(round => {

    if (fromProfile) {
        round.addEventListener("click", () => {
            const roundNumber = round.getAttribute("data-round");
            window.location.href = `/profile/best-game/summary/round=${roundNumber}`;
        });
    } else {
        round.addEventListener("click", () => {
            const roundNumber = round.getAttribute("data-round");
            window.location.href = `/summary/round=${roundNumber}`;
        });
    }

});





