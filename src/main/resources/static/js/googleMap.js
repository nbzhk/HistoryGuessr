const {Map} = await google.maps.importLibrary("maps");
const {LatLng} = await google.maps.importLibrary("core");
const yearSlider = document.getElementById("yearSlider");
const yearValue = document.getElementById("yearValue");

const map = new Map(document.getElementById("googleMap"), {
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
});


yearValue.textContent = yearSlider.value;

yearSlider.addEventListener("input", function() {
    yearValue.textContent = this.value;
});

let guessLat;
let guessLng;

function position(mapsMouseEvent) {
    guessLat = mapsMouseEvent.latLng.lat();
    guessLng = mapsMouseEvent.latLng.lng();
}

map.addListener("click", position);

function fetchCoordinates() {
    let guessYear = yearSlider.value;
    fetch("/game", {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({guessLat, guessLng, guessYear})

    }).then(response => {

        window.location.href = response.url;
    });
}


const fetchButton = document.getElementById("fetchButton");

fetchButton.addEventListener("click", () => {
    fetchCoordinates();
});

const expandable = document.getElementById("expandable");
expandable.addEventListener("mouseenter", enlargeFunc);

function enlargeFunc() {
    console.log("start!");
    expandable.style.width = "800px";
    expandable.style.height = "800px";
}

expandable.addEventListener("mouseleave", decrementFunc);

function decrementFunc() {
    console.log("end!");
    expandable.style.width = "150px";
    expandable.style.height = "150px";
}

// document.addEventListener("DOMContentLoaded", function () {
//     console.log("DOMContentLoaded event fired");
//     const expandable = document.querySelector(".expandable");
//     console.log(expandable);
//
//     expandable.addEventListener("mouseenter", function () {
//         console.log("mouse in");
//         this.classList.add("expanded");
//     });
//
//     expandable.addEventListener("mouseleave", function () {
//         console.log("mouse out");
//         this.classList.remove("expanded");
//     });
// });








