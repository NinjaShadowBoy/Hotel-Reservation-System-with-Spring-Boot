function login(){
  document.getElementById("login-form").addEventListener("submit", function(e) {

      e.preventDefault(); // Prevent the default form submission
        const formData = new FormData(this);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        // // Check if jsonData is empty
        // if (Object.keys(jsonData).length === 0) {
        //     console.error("Error: jsonData is empty!");
        //     document.getElementById("login-result").innerHTML = "Error: No data to send. Please fill out the form.";
        //     return;
        // }
        // console.log("Sending data:", JSON.stringify(jsonData));


        // Create the JSON string
        const jsonString = JSON.stringify(jsonData);
        console.log("JSON string:", jsonString);

        // // Check if the JSON string is valid
        // try {
        //     JSON.parse(jsonString);
        //     console.log("JSON is valid");
        // } catch (e) {
        //     console.error("JSON is invalid:", e);
        //     document.getElementById("login-result").innerHTML = "Error: Invalid JSON data.";
        //     return;
        // }

        fetch("/api/owner/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: jsonString,
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok " + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            console.log("Success:", data);
            document.getElementById("login-result").innerHTML = "Login successful!";
            // Redirect to the owner dashboard or another page
            window.location.href = "/owner/home";
            localStorage.setItem("user", JSON.stringify(data));
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("login-result").innerHTML = "Error: " + error.message;
        });
        console.log("Form submitted successfully");

  });
}

// Call the login function when the page loads
document.addEventListener('DOMContentLoaded', function() {
  login();
});
