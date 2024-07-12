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
            console.log(response.headers)
            window.location.href = redirectUrl;
        } else {
            console.log(response);
            console.log(response.headers.get("X-XSRF-TOKEN"))
            console.log("not redirecting");
        }
    })
}


const fetchButton = document.getElementById("fetchButton");

fetchButton.addEventListener("click", () => {
    fetchCoordinates();
    console.log("fetched")
});



const expandable = document.getElementById("expandable");
const googleMap = document.getElementById("googleMap");
expandable.addEventListener("mouseenter", enlargeFunc);

function enlargeFunc() {
    expandable.style.width = "45vw"
    googleMap.style.height = "47vh"
}

expandable.addEventListener("mouseleave", decrementFunc);

function decrementFunc() {
    expandable.style.width = "17vw"
    googleMap.style.height = "23vh"
}


let photoContainer = document.getElementById('photoContainer');
let backgroundSize = 100;

photoContainer.addEventListener("wheel", (event) => {

    if (event.deltaY < 0) {
        backgroundSize += 2;
    } else {
        if (backgroundSize > 100) {
            backgroundSize -= 2;
        } else {
            return;
        }
    }

    backgroundSize = Math.max(backgroundSize, 100);

    if (backgroundSize === 100 && photoContainer.style.backgroundSize !== 'contain') {
        photoContainer.style.removeProperty('background-size');
        photoContainer.style.backgroundSize = `contain`;
    } else {
        photoContainer.style.backgroundSize = `${backgroundSize}%`;
    }

});


let isDragging = false;
let x = 0;
let y = 0;

photoContainer.addEventListener("mousedown", (event) => {
    isDragging = true;
    x = event.clientX;
    y = event.clientY;
});


photoContainer.addEventListener("mousemove", (event) => {
    if (isDragging) {
        const deltaX = event.clientX - x;
        const deltaY = event.clientY - y;

        const backgroundPositionX = parseInt(photoContainer.style.backgroundPositionX) || 0;
        const backgroundPositionY = parseInt(photoContainer.style.backgroundPositionY) || 0;

        photoContainer.style.backgroundPositionX = `${backgroundPositionX + deltaX}px`;
        photoContainer.style.backgroundPositionY = `${backgroundPositionY + deltaY}px`;

        x = event.clientX;
        y = event.clientY;


        adjustBackgroundPosition();

    }
});

photoContainer.addEventListener("mouseup", () => {
    isDragging = false;
});

function adjustBackgroundPosition() {
    const containerRect = photoContainer.getBoundingClientRect();
    const bgWidth = (backgroundSize / 100) * containerRect.width;
    const bgHeight = (backgroundSize / 100) * containerRect.height;

    let bgPosX = parseInt(photoContainer.style.backgroundPositionX) || 0;
    let bgPosY = parseInt(photoContainer.style.backgroundPositionY) || 0;

    const minX = containerRect.width - bgWidth;
    const minY = containerRect.height - bgHeight;

    if (bgPosX > 0) bgPosX = 0;
    if (bgPosY > 0) bgPosY = 0;
    if (bgPosX < minX) bgPosX = minX;
    if (bgPosY < minY) bgPosY = minY;

    photoContainer.style.backgroundPositionX = `${bgPosX}px`;
    photoContainer.style.backgroundPositionY = `${bgPosY}px`;
}
