const yearSlider = document.getElementById("yearSlider");
const yearValue = document.getElementById("yearValue");


yearValue.textContent = yearSlider.value;

yearSlider.addEventListener("input", function() {
    yearValue.textContent = this.value;
});
