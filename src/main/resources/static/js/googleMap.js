const {Map} = await google.maps.importLibrary("maps");
console.log("importLibrary MAPS")
const {LatLng} = google.maps.importLibrary("core");
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

function fetchCoordinates() {
    guessYear = yearSlider.value;

    fetch("/game", {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({guessLat, guessLng, guessYear})

    }).then(response => {
        if (response.ok) {
            console.log("ok!!!")
            window.location.href = "/result";
        } else {
            console.log("not redirecting")
        }
    })
}


const fetchButton = document.getElementById("fetchButton");

fetchButton.addEventListener("click", () => {
    fetchCoordinates();
    console.log("fetched")
});

const expandable = document.getElementById("expandable");
expandable.addEventListener("mouseenter", enlargeFunc);

function enlargeFunc() {
    expandable.style.width = "700px";
    expandable.style.height = "700px";
}

expandable.addEventListener("mouseleave", decrementFunc);

function decrementFunc() {
    expandable.style.width = "150px";
    expandable.style.height = "150px";
}

const container = document.getElementById("container");
const imageZoom = document.getElementById("image-zoom");
let scale = 1;

container.addEventListener("wheel", zoomFunc);

function zoomFunc(event) {
    console.log("zoooooom!");
    event.preventDefault();

    const delta = Math.max(-1, Math.min(1, event.deltaY))

    if (delta > 0) {
        scale -= 0.1;
    } else {
        scale += 0.1;
    }

    imageZoom.style.transform = `scale(${scale})`;
}