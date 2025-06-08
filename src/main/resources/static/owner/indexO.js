function registerHotels() {
    document.getElementById('add-hotel-form').addEventListener('submit', function(e){
        e.preventDefault();

        const formData = new FormData(this);
        const jsonData = {};

        const hotelCards = document.getElementById('HotelCards');

        const user = JSON.parse(localStorage.getItem('user'));

        console.log(user);

        formData.append("id", user.id);

        // Log all form data
        console.log('Form data:');
        for (let [key, value] of formData.entries()) {
            console.log(`${key} : ${value}`);
        }

        // Map form field names to entity property names
        const fieldMapping = {
            'owner-id': 'id',
            'hotel-name': 'name',
            'hotel-location': 'location',
            'hotel-rating': 'rating',
            'hotel-description': 'description'
        };

        formData.forEach((value, key) => {
            const mappedKey = fieldMapping[key] || key;
            // Convert rating to a number
            if (mappedKey === 'rating') {
                jsonData[mappedKey] = parseFloat(value);
            } else if(mappedKey === 'id') {
                jsonData[mappedKey] = parseInt(value, 10);
            }
            else{
                jsonData[mappedKey] = value;
            }
        });

        // Check if jsonData is empty
        if (Object.keys(jsonData).length === 0) {
            console.error('Error: jsonData is empty!');
            document.getElementById('register-result').innerHTML = 'Error: No data to send. Please fill out the form.';
            return;
        }

        console.log('Sending data:', JSON.stringify(jsonData));

        // Create the JSON string
        const jsonString = JSON.stringify(jsonData);
        console.log('JSON string:', jsonString);

        // Check if the JSON string is valid
        try {
            JSON.parse(jsonString);
            console.log('JSON is valid');
        } catch (e) {
            console.error('JSON is invalid:', e);
            document.getElementById('register-result').innerHTML = 'Error: Invalid JSON data.';
            return;
        }

        fetch('/api/hotels/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: jsonString,
        })
            .then(response => {
                console.log('Response status:', response.status);
                console.log('Response headers:', [...response.headers.entries()]);

                // Clone the response to log its content
                return response.clone().text().then(text => {
                    console.log('Response text:', text);

                    if (!response.ok) {
                        try {
                            const data = JSON.parse(text);
                            console.error('Error data:', data);
                            document.getElementById('register-result').innerHTML =
                                `Error ${response.status}: ${JSON.stringify(data)}`;
                            throw new Error(`Error ${response.status}: ${JSON.stringify(data)}`);
                        } catch (e) {
                            console.error('Error parsing response:', e);
                            document.getElementById('register-result').innerHTML =
                                `Error ${response.status}: ${text}`;
                            throw new Error(`Error ${response.status}: ${text}`);
                        }
                    }

                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Error parsing response:', e);
                        document.getElementById('register-result').innerHTML =
                            `Error parsing response: ${e.message}`;
                        throw new Error(`Error parsing response: ${e.message}`);
                    }
                });
            })
            .then(data => {
                console.log('Success data:', data);

                const card = document.createElement('div');
                card.className = 'hotel-card';
                card.innerHTML = `
                <div class="hotel-image" style="background-image: url('${data.image || '/images/placeholder.png'}')"></div>
                <div class="hotel-card-body">
                    <h3 class="hotel-name">${data.name || 'Unnamed Hotel'}</h3>
                    <div class="hotel-location">
                        <i class="fas fa-map-marker-alt"></i> ${data.location || 'Location not specified'}
                    </div>
                    <div class="hotel-stats">
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">--</div>
                            <div class="hotel-stat-label">Rooms</div>
                        </div>
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">-- %</div>
                            <div class="hotel-stat-label">Occupancy</div>
                        </div>
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">${data.rating || '0.0'}</div>
                            <div class="hotel-stat-label">Rating</div>
                        </div>
                    </div>
                </div>           
            `;
                hotelCards.appendChild(card);


                document.getElementById('register-result').innerHTML =
                    `Hotel added successfully: ${JSON.stringify(data)}`;

                // Show toast notification if the function exists
                if (typeof showToast === 'function') {
                    showToast(`Hotel "${data.name}" added successfully`);
                }

                // Reset form
                document.getElementById('add-hotel-form').reset();

                // Clear photo preview if it exists
                const photoPreview = document.getElementById('photo-preview');
                if (photoPreview) {
                    photoPreview.innerHTML = '';
                }

                // Switch to dashboard tab if the element exists
                const dashboardTab = document.querySelector('.nav-item[data-tab="dashboard"]');
                if (dashboardTab) {
                    dashboardTab.click();
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                document.getElementById('register-result').innerHTML =
                    `Error: ${error.message}`;
            });

    })
}

function getOwnerHotels() {
    const hotelCards = document.getElementById('HotelCards');
    const selectHotel = document.getElementById('select-hotel');
    const filterHotel = document.getElementById('hotel-review-filter');
    
    // Clear existing content
    hotelCards.innerHTML = '';
    selectHotel.innerHTML = '<option value="">Select a hotel</option>';

    const user = JSON.parse(localStorage.getItem('user'));
    const ownerId = user.id;

    if (!user || !user.id) {
        console.error('No user found or invalid user ID');
        hotelCards.innerHTML = '<p>Error: Please log in to view your hotels</p>';
        return;
    }

    console.log('Owner ID:', ownerId);

    // // Show loading state
    // hotelCards.innerHTML = '<p>Loading hotels...</p>';

    fetch(`/api/owner/${encodeURIComponent(ownerId)}/hotels`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Owner Hotels:', data);
        
        if (!Array.isArray(data) || data.length === 0) {
            hotelCards.innerHTML = '<p>No hotels found. Add your first hotel!</p>';
            return;
        }

        data.forEach(hotel => {
            if (!hotel) return; // Skip null hotels
            
            const card = document.createElement('div');
            card.className = 'hotel-card';
            card.innerHTML = `
                <div class="hotel-image" style="background-image: url('${hotel.image || '/images/placeholder.png'}')"></div>
                <div class="hotel-card-body">
                    <h3 class="hotel-name">${hotel.name || 'Unnamed Hotel'}</h3>
                    <div class="hotel-location">
                        <i class="fas fa-map-marker-alt"></i> ${hotel.location || 'Location not specified'}
                    </div>
                    <div class="hotel-stats">
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">--</div>
                            <div class="hotel-stat-label">Rooms</div>
                        </div>
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">-- %</div>
                            <div class="hotel-stat-label">Occupancy</div>
                        </div>
                        <div class="hotel-stat">
                            <div class="hotel-stat-value">${hotel.rating || '0.0'}</div>
                            <div class="hotel-stat-label">Rating</div>
                        </div>
                    </div>
                </div>           
            `;
            hotelCards.appendChild(card);

            // Add option to select hotel
            const option = document.createElement('option');
            option.value = hotel.id;
            option.textContent = hotel.name || 'Unnamed Hotel';
            selectHotel.appendChild(option);
            filterHotel.appendChild(option);
        });

        // Add event listener for hotel selection only once
        selectHotel.removeEventListener('change', handleHotelSelection);
        selectHotel.addEventListener('change', handleHotelSelection);

        filterHotel.removeEventListener('change', handleHotelSelectionForReviews);
        filterHotel.addEventListener('change', handleHotelSelectionForReviews);
    })
    .catch(error => {
        console.error('Error fetching owner hotels:', error);
        hotelCards.innerHTML = `
            <div class="error-message">
                <p>Error loading hotels: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
    });
}

// Separate function for handling hotel selection for room types
function handleHotelSelection(e) {
    const value = e.target.value;
    const hotelRooms = document.getElementById('rooms-body');
    const addRoomButton = document.getElementById('add-room-button');
    const addRoomModal = document.getElementById('add-room-modal');
    const user = JSON.parse(localStorage.getItem('user'));
    const ownerId = user.id;

    // Clear existing room types
    hotelRooms.innerHTML = '';

    if (!value || value === 'none') {
        hotelRooms.innerHTML = '<p>Please select a hotel to display its room types</p>';
        if (addRoomButton) {
            addRoomButton.style.display = 'none';
        }
        if (addRoomModal) {
            addRoomModal.style.display = 'none';
        }
        console.log('No hotel selected');
        localStorage.setItem('CurrentHotelId', 'none');
        console.log('currently ', localStorage.getItem('CurrentHotelId'));
        return;
    }

    console.log('Selected hotel id:', value);
    localStorage.setItem('CurrentHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('CurrentHotelId'));

    // Show add room button
    if (addRoomButton) {
        addRoomButton.style.display = 'block';
    }

    fetch(`/api/owner/${encodeURIComponent(ownerId)}/${encodeURIComponent(value)}/roomtypes`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Hotel room types:', data);

        if (!Array.isArray(data) || data.length === 0) {
            hotelRooms.innerHTML = '<p>No room types found. Add your first room type!</p>';
            return;
        }

        data.forEach(roomtype => {
            if (!roomtype) return; // Skip null room types

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${roomtype.label}</td>
                <td>
                    <span class="price-display">$${roomtype.price}</span>
                    <input type="number" class="price-edit" style="display: none;" value="${roomtype.price}">
                </td>
                <td>
                    <span class="price-display">${roomtype.numberAvailable}</span>
                    <input type="number" class="price-edit" style="display: none;" value="${roomtype.numberAvailable}">
                </td>
                <td>
                    <button class="btn btn-secondary btn-sm edit-price">
                        <i class="fas fa-edit"></i> Edit Price
                    </button>
                    <button class="btn btn-danger btn-sm">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            `;
            hotelRooms.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error fetching room types:', error);
        hotelRooms.innerHTML = `
            <div class="error-message">
                <p>Error loading room types: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
    });
}


// Separate function for handling hotel selection for reviews
function handleHotelSelectionForReviews(e) {
    const value = e.target.value;
    const hotelReviews = document.getElementById('review-body');

    const currentUser = JSON.parse(localStorage.getItem('user'));
    const ownerId = currentUser.id;


    if (value === 'all') {

        localStorage.setItem('ReviewsHotelId', 'all');
        console.log('currently ', localStorage.getItem('ReviewsHotelId'));
        return;  //to be touched
    }


    hotelReviews.innerHTML = '';

    console.log('Selected hotel id:', value);
    localStorage.setItem('ReviewsHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('ReviewsHotelId'));


    fetch(`/api/reviews/${encodeURIComponent(value)}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Hotel reviews:', data);

            if (!Array.isArray(data) || data.length === 0) {
                hotelReviews.innerHTML = '<p>No reviews for this hotel yet!</p>';
                return;
            }

            data.forEach(review => {
                if (!review) return; // Skip null room types

                const reviewDiv = document.createElement('div');
                reviewDiv.className = 'review-card';
                reviewDiv.innerHTML = `
                        <div class="review-header">
                          <div>
                            <span class="review-author">${review.author}</span>
                            <span class="review-date">${review.date}</span>
                          </div>
                          <div class="review-rating">
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                          </div>
                        </div>
                    <p class="review-text">${review.text}</p>
                   
            `;
                hotelReviews.appendChild(reviewDiv);

                const starsContainer = reviewDiv.querySelector('.review-rating');
                simpleStarUpdate(review.rating, starsContainer);
            });
        })
        .catch(error => {
            console.error('Error fetching reviews:', error);
            hotelReviews.innerHTML = `
            <div class="error-message">
                <p>Error loading room types: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
        });
}

function simpleStarUpdate(rating, starsContainer) {
    const stars = starsContainer.querySelectorAll('.fa-star');
    const filledStars = Math.round(rating);

    stars.forEach((star, index) => {
        if (index < filledStars) {
            star.className = 'fas fa-star'; // filled
        } else {
            star.className = 'far fa-star'; // empty
        }
    });
}



function getServices(){
    const services = document.getElementById('service-elements');

    fetch(`/api/services`)
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Success data in native form:', data);

        data.forEach(serv => {
            const parts = serv.toString().split(':');
            const serviceName = parts[0];
            const iconClass = parts[1] || 'fas fa-check'; // Default icon if none provided
            const serviceId = `amenity-${parts[2] || serviceName.toLowerCase().replace(/\s+/g, '-')}`;

            const subservice = document.createElement('div');
            subservice.className = 'checkbox-item';

            subservice.innerHTML = `
            <div class="checkbox-item">
              <input type="checkbox" id="${serviceId}" name="amenity" value="${serviceName}">
              <label for="amenity-${serviceId}"> <i class="${iconClass}"></i> ${serviceName}</label>
            </div>
        `
            services.appendChild(subservice);
        });
    })
    .catch(error => {
        console.error('Error fetching services: ', error);
        services.innerHTML = `<p>Error fetching services: ${error.message}</p>`;
    })
}

function addRoomType(){
    document.getElementById('add-room-form').addEventListener('submit', function(e) {
        e.preventDefault();

        const hotelId = localStorage.getItem('CurrentHotelId');
        if (!hotelId || hotelId === 'none') {
            alert('Please select a hotel first before adding a room type!');
            return;
        }

        console.log(this);
        const formData = new FormData(this);
        const jsonData = {};

        const user = JSON.parse(localStorage.getItem('user'));
        const ownerId = user.id;

        console.log('hotel, owner: ' + hotelId + " " + ownerId);

        jsonData['services'] = formData.getAll('amenity');
        console.log(jsonData['services']);

        formData.forEach((value, key) => {
            if(key !== 'amenity'){
                jsonData[key] = value;
            }
        });

        const jsonstring = JSON.stringify(jsonData);
        console.log('resulting json: ' + jsonstring);

        // Show loading state
        const submitButton = this.querySelector('button[type="submit"]');
        const originalText = submitButton ? submitButton.textContent : '';
        if (submitButton) {
            submitButton.disabled = true;
            submitButton.textContent = 'Adding Room...';
        }

        fetch(`/api/owner/${encodeURIComponent(ownerId)}/${encodeURIComponent(hotelId)}/roomType/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: jsonstring,
        })
        .then(response => {
            console.log('Response status:', response.status);
            console.log('Response headers:', [...response.headers.entries()]);

            return response.text().then(text => {
                console.log('Response text:', text);

                if (!response.ok) {
                    throw new Error(text || `Server Error (${response.status})`);
                }

                try {
                    return JSON.parse(text);
                } catch (parseError) {
                    console.warn('Response is not JSON, treating as success');
                    return { success: true, message: text };
                }
            });
        })
        .then(data => {
            console.log('Room added successfully:', data);

            // Reset form
            document.getElementById('add-room-form').reset();

            // Close modal
            const modal = document.getElementById('add-room-modal');
            if (modal) {
                modal.style.display = 'none';
            }

            // Show success message
            alert('Room Added successfully!');

            // Refresh the room list
            const selectHotel = document.getElementById('select-hotel');
            if (selectHotel) {
                selectHotel.dispatchEvent(new Event('change'));
            }
        })
        .catch(error => {
            console.error('Error adding room type:', error);
            alert(`Failed to add room: ${error.message}`);
        })
        .finally(() => {
            // Restore button state
            if (submitButton) {
                submitButton.disabled = false;
                submitButton.textContent = originalText;
            }
        });
    });
}

// Add this new function to handle the add room button click
function handleAddRoomButtonClick() {
    const hotelId = localStorage.getItem('CurrentHotelId');
    const addRoomModal = document.getElementById('add-room-modal');
    
    if (!hotelId || hotelId === 'none') {
        alert('Please select a hotel first before adding a room type!');
        return;
    }
    
    if (addRoomModal) {
        addRoomModal.style.display = 'block';
    }
}

document.addEventListener('DOMContentLoaded', function(){
    localStorage.setItem('CurrentHotelId', 'none');

    getServices();
    registerHotels();
    getOwnerHotels();
    addRoomType();

    // Add event listener for the add room button
    const addRoomButton = document.getElementById('add-room-button');
    if (addRoomButton) {
        addRoomButton.addEventListener('click', handleAddRoomButtonClick);
    }
});
