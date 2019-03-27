function showSlideValue(sliderId, spanId) {
    var value = document.getElementById(sliderId).value;
    var span = document.getElementById(spanId);
    span.innerHTML = "Value: " + value / 10;
}