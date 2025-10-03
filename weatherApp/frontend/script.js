async function getWeather() {
  const city = document.getElementById("city").value;
  if (!city) {
    alert("Please enter a city!");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/weather?city=${city}`);
    const data = await response.json();

    if (data.error) {
      document.getElementById("result").innerHTML = `<p style="color:red;">${data.error}</p>`;
      return;
    }

    document.getElementById("result").innerHTML = `
      <h2>${data.city}</h2>
      <p>🌡 Temperature: ${data.temp} °C</p>
      <p>🌥 Condition: ${data.description}</p>
    `;
  } catch (error) {
    document.getElementById("result").innerHTML = `<p style="color:red;">Something went wrong!</p>`;
  }
}
