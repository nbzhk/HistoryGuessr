const {Map} = await google.maps.importLibrary("maps");
const {Marker} = await google.maps.importLibrary("marker");
const {LatLng} = await google.maps.importLibrary("core");

let map = new Map(document.getElementById("uploadMap"), {
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


let marker = null;

map.addListener("click", (mapsMouseEvent) =>  {

    if (marker) {
        marker.setPosition(mapsMouseEvent.latLng);
    } else {
        marker = new google.maps.Marker({
            position: mapsMouseEvent.latLng,
            map: map,
            title: "My Guess!"
        })
    }

    let newLat = marker.getPosition().lat().toFixed(6);
    let newLng = marker.getPosition().lng().toFixed(6);

    document.getElementById("latitude").value = newLat;
    document.getElementById("longitude").value = newLng;
})

let latInput = document.getElementById("latitude");
let lngInput = document.getElementById("longitude");

latInput.addEventListener("change", (event) => {
    const newLat = parseFloat(latInput.value);

    if (marker) {
        marker.setPosition({lat: newLat,lng: marker.getPosition().lng()});
    }
})

lngInput.addEventListener("change", (event) => {
    const newLng = parseFloat(lngInput.value);

    if (marker) {
        marker.setPosition({lat: marker.getPosition().lat(), lng: newLng});
    }
})

