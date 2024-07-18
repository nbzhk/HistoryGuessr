const {Map} = await google.maps.importLibrary("maps");
const {LatLng} = google.maps.importLibrary("core");
const yearSlider = document.getElementById("yearSlider");
const yearValue = document.getElementById("yearValue");

const isDaily = document.body.classList.contains("daily-challenge");


let map;

map = new Map(document.getElementById("googleMap"), {
    zoom: 0,
    center: {lat: 0, lng: 0},
    mapTypeControl: false,
    fullscreenControl: false,
    streetViewControl: false,
    zoomControl: false,
    keyboardShortcuts: false,
    minZoom: 1,
    restriction: {
        latLngBounds: {
            north: 85,
            south: -85,
            west: -180,
            east: 180,
        }
    },
    styles: [
        {elementType: 'geometry', stylers: [{color: 'transparent'}]}
    ]
});
let guessMarker = null;

map.addListener("click", (mapsMouseEvent) => {
    if (guessMarker) {
        guessMarker.setPosition(mapsMouseEvent.latLng);
    } else {
        guessMarker = new google.maps.Marker({
            position: mapsMouseEvent.latLng,
            map: map,
            title: "My Guess!"
        });
    }
    console.log("marker SET")
    fetchButton.disabled = false;
    fetchButton.textContent = "Submit"
});


yearValue.textContent = yearSlider.value;

yearSlider.addEventListener("input", function () {
    yearValue.textContent = this.value;
    console.log("year Value change")
});

let guessLat;
let guessLng;
let guessYear;

function position(mapsMouseEvent) {
    guessLat = mapsMouseEvent.latLng.lat();
    guessLng = mapsMouseEvent.latLng.lng();
    console.log("set LAt and Lng")
}

map.addListener("click", position);


async function fetchCoordinates() {

    const csrfToken = document.querySelector('meta[name="_csrf"]');

    const token = csrfToken.getAttribute("content");

    guessYear = yearSlider.value;

    let inputUrl;
    let redirectUrl;

    if (isDaily) {
        inputUrl = "/daily/make-guess";
        redirectUrl = "/daily/result"
    } else {
        inputUrl = "/game/get-user-guess";
        redirectUrl = "/result"
    }

    fetch(inputUrl, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': token
        },
        body: JSON.stringify({guessLat, guessLng, guessYear})

    }).then(response => {
        if (response.ok) {
            window.location.href = redirectUrl;
        } else {
            console.log(response);
        }
    })
}


const fetchButton = document.getElementById("fetchButton");

fetchButton.addEventListener("click", () => {
    fetchCoordinates().then(() => console.log("fetched"));
});
