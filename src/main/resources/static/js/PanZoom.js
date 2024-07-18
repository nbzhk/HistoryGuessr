let photoContainer = document.getElementById('photoContainer');
let backgroundSize = 100;
let isPanning = false;
let startX = 0;
let startY = 0;
let initialPosX = 50; // Starting center position X
let initialPosY = 50; // Starting center position Y

// Zoom functionality
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

// Pan functionality
photoContainer.addEventListener('mousedown', (event) => {
    isPanning = true;
    startX = event.clientX;
    startY = event.clientY;
});

document.addEventListener('mousemove', (event) => {
    if (!isPanning) return;

    const dx =  startX - event.clientX ;
    const dy = startY - event.clientY;

    // Calculate new background positions
    let posX = initialPosX + (dx / photoContainer.clientWidth) * 100;
    let posY = initialPosY + (dy / photoContainer.clientHeight) * 100;

    // Constrain the panning limits
    posX = Math.max(0, Math.min(100, posX));
    posY = Math.max(0, Math.min(100, posY));

    photoContainer.style.backgroundPosition = `${posX}% ${posY}%`;
});

document.addEventListener('mouseup', (event) => {
    if (isPanning) {
        const dx = startX - event.clientX ;
        const dy = startY - event.clientY;
        initialPosX += (dx / photoContainer.clientWidth) * 100;
        initialPosY += (dy / photoContainer.clientHeight) * 100;

        initialPosX = Math.max(0, Math.min(100, initialPosX));
        initialPosY = Math.max(0, Math.min(100, initialPosY));
    }
    isPanning = false;
});

photoContainer.addEventListener('mouseleave', () => {
    isPanning = false;
});

//Map Container Expand
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
