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


function updateHotel() {
    document.getElementById('edit-hotel-form').addEventListener('submit', function(e) {
        e.preventDefault();

        const hotelId = localStorage.getItem('updatesHotelId');
        const formData = new FormData(this);

        // Ensure hotelName is present
        if (!formData.get('hotelName')) {
            alert('Hotel name is required');
            return;
        }

        fetch(`/api/hotels/${encodeURIComponent(hotelId)}/update`, {
            method: 'PUT',
            body: formData
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
                console.log('updated Hotel:', data);

                if(data.id === hotelId) {
                    const hotelCard = document.getElementById(`hotel-${hotelId}`);
                    if (hotelCard) {
                        hotelCard.querySelector('.hotel-name').textContent = data.name || 'Unnamed Hotel';
                        hotelCard.querySelector('.hotel-image').style.backgroundImage = `url('${data.image || '/images/placeholder.png'}')`;
                    }
                    alert('Hotel updated successfully!');
                }
                else {
                    console.error('Hotel ID mismatch after update:', data.id, hotelId);
                    alert('Error: Hotel ID mismatch after update. Please try again.');
                }
            })
            .catch(error => {
                console.error('Error updating hotel:', error);
                alert('Error updating hotel: ' + error.message);
            });
    });
}

function getOwnerHotels() {
    const hotelCards = document.getElementById('HotelCards');
    const selectHotel = document.getElementById('select-hotel');
    const filterHotel = document.getElementById('hotel-review-filter');
    const filterHotelQuestion = document.getElementById('hotel-question-filter');
    const filterHotelFAQ = document.getElementById('hotel-faq-filter');
    const filterHotelBooking = document.getElementById('hotel-booking-filter');
    const hotelsForUpdate = document.getElementById('update-hotel');



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
            card.id = `hotel-${hotel.id}`;
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
            const option1 = document.createElement('option');
            option1.value = hotel.id;
            option1.textContent = hotel.name || 'Unnamed Hotel';
            selectHotel.appendChild(option1);

            const option2 = document.createElement('option');
            option2.value = hotel.id;
            option2.textContent = hotel.name || 'Unnamed Hotel';
            filterHotel.appendChild(option2);

            const option3 = document.createElement('option');
            option3.value = hotel.id;
            option3.textContent = hotel.name || 'Unnamed Hotel';
            filterHotelQuestion.appendChild(option3);

            const option4 = document.createElement('option');
            option4.value = hotel.id;
            option4.textContent = hotel.name || 'Unnamed Hotel';
            filterHotelFAQ.appendChild(option4);

            const option5 = document.createElement('option');
            option5.value = hotel.id;
            option5.textContent = hotel.name || 'Unnamed Hotel';
            filterHotelBooking.appendChild(option5);

            const option6 = document.createElement('option');
            option6.value = hotel.id;
            option6.textContent = hotel.name || 'Unnamed Hotel';
            hotelsForUpdate.appendChild(option6);
        });

        // Add event listener for hotel selection only once
        selectHotel.removeEventListener('change', handleHotelSelection);
        selectHotel.addEventListener('change', handleHotelSelection);

        filterHotel.removeEventListener('change', handleHotelSelectionForReviews);
        filterHotel.addEventListener('change', handleHotelSelectionForReviews);

        filterHotelQuestion.removeEventListener('change', handleHotelSelectionForQuestions);
        filterHotelQuestion.addEventListener('change', handleHotelSelectionForQuestions);

        filterHotelFAQ.removeEventListener('change', handleHotelSelectionForFaq);
        filterHotelFAQ.addEventListener('change', handleHotelSelectionForFaq);

        filterHotelBooking.removeEventListener('change', handleHotelSelectionForBooking);
        filterHotelBooking.addEventListener('change', handleHotelSelectionForBooking);

        hotelsForUpdate.removeEventListener('change', handleHotelSelectionForUpdate);
        hotelsForUpdate.addEventListener('change', handleHotelSelectionForUpdate);
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
                    <button id="edit-roomtype-${roomtype.id}" class="btn btn-secondary btn-sm edit-price">
                        <i class="fas fa-edit"></i> Edit Room type
                    </button>
                </td>
            `;
            hotelRooms.appendChild(row);
        });

                // Add event listeners to the edit buttons
                data.forEach(roomtype => {
                    const editRoomModal = document.getElementById('edit-roomtype-modal');
                    const closeModal = document.querySelectorAll('.close-modal');                    const editButton = document.getElementById(`edit-roomtype-${roomtype.id}`);
                    if (editButton) {
                        editButton.addEventListener('click', () => {
                            document.getElementById('price').value = roomtype.price;
                            document.getElementById('totalnumber').value = roomtype.totalnumber;
                            editRoomModal.style.display = 'flex'; // Function to show the modal
                        });
                    }
                    if (closeModal[2]) {
                        closeModal[2].addEventListener('click', function() {
                            editRoomModal.style.display = 'none';
                        });
                    }
                    window.addEventListener('click', function(e) {
                        if (e.target === editRoomModal) {
                            editRoomModal.style.display = 'none';}
                    });
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

        hotelReviews.innerHTML = '<p>Please select a hotel to display its Reviews</p>';
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


// Separate function for handling hotel selection for questions
function handleHotelSelectionForQuestions(e) {
    const value = e.target.value;
    const hotelQuestions = document.getElementById('question-body');

    const currentUser = JSON.parse(localStorage.getItem('user'));
    const ownerId = currentUser.id;


    if (value === 'all') {

        hotelQuestions.innerHTML = '<p>Please select a hotel to display its Questions</p>';
        localStorage.setItem('QuestionsHotelId', 'all');
        console.log('currently ', localStorage.getItem('QuestionsHotelId'));
        return;  //to be touched
    }


    hotelQuestions.innerHTML = '';

    console.log('Selected hotel id:', value);
    localStorage.setItem('QuestionsHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('QuestionsHotelId'));


    fetch(`/api/question/${encodeURIComponent(value)}`, {
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
            console.log('Hotel questions:', data);

            if (!Array.isArray(data) || data.length === 0) {
                hotelQuestions.innerHTML = '<p>No questions for this hotel yet!</p>';
                return;
            }

            data.forEach(question => {
                if (!question) return; // Skip null room types

                const questionDiv = document.createElement('div');
                questionDiv.className = 'review-card';
                questionDiv.innerHTML = `
                        <div class="review-header">
                          <div>
                            <span class="review-author">${question.question}</span>
                          </div>
                        </div>                
                   
            `;
                hotelQuestions.appendChild(questionDiv);
            });
        })
        .catch(error => {
            console.error('Error fetching reviews:', error);
            hotelQuestions.innerHTML = `
            <div class="error-message">
                <p>Error loading questions: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
        });
}


// Separate function for handling hotel selection for Faqs
function handleHotelSelectionForFaq(e) {
    const value = e.target.value;
    const hotelFaqs = document.getElementById('faq-body');

    const currentUser = JSON.parse(localStorage.getItem('user'));
    const ownerId = currentUser.id;


    if (value === 'all') {

        hotelFaqs.innerHTML = '<p>Please select a hotel to display its FAQs</p>';
        localStorage.setItem('FaqsHotelId', 'all');
        console.log('currently ', localStorage.getItem('FaqsHotelId'));
        return;  //to be touched
    }


    hotelFaqs.innerHTML = '';

    console.log('Selected hotel id:', value);
    localStorage.setItem('FaqsHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('FaqsHotelId'));


    fetch(`/api/faqs/${encodeURIComponent(value)}`, {
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
            console.log('Hotel Faqs:', data);

            if (!Array.isArray(data) || data.length === 0) {
                hotelFaqs.innerHTML = '<p>No FAQs for this hotel yet! Add one</p>';
                return;
            }

            data.forEach(faq => {
                if (!faq) return; // Skip null room types

                const faqDiv = document.createElement('div');
                faqDiv.className = 'review-card';
                faqDiv.innerHTML = `
                        <div class="review-header">
                          <div>
                            <span class="review-author">Question:  </span><span class="review-author">${faq.question}</span>
                          
                          </div>
                         
                        </div>
                    <span class="review-author">Answer:  </span><span>${faq.answer}</span>         
                   
            `;
                hotelFaqs.appendChild(faqDiv);
            });
        })
        .catch(error => {
            console.error('Error fetching reviews:', error);
            hotelFaqs.innerHTML = `
            <div class="error-message">
                <p>Error loading questions: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
        });
}



// Separate function for handling hotel selection for questions
function handleHotelSelectionForBooking(e) {
    const value = e.target.value;
    const hotelBookings = document.getElementById('hotel-bookings');

    if (value === 'all') {

        hotelBookings.innerHTML = '<p>Please select a hotel to display its bookings</p>';
        localStorage.setItem('BookingsHotelId', 'all');
        console.log('currently ', localStorage.getItem('BookingsHotelId'));
        return;  //to be touched
    }


    hotelBookings.innerHTML = '';

    console.log('Selected hotel id:', value);
    localStorage.setItem('BookingsHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('BookingsHotelId'));


    fetch(`/api/bookings/hotels/${encodeURIComponent(value)}`, {
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
            console.log('Hotel bookings:', data);

            if (!Array.isArray(data) || data.length === 0) {
                hotelBookings.innerHTML = '<p>No bookings for this hotel yet!</p>';
                return;
            }

            data.forEach(booking => {
                if (!booking) return; // Skip null room types

                fetch(`/api/bookings/hotels/${encodeURIComponent(booking.clientId)}`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }).then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                        });
                    }
                    return response.json();
                })
                    .then(data => {
                        console.log('Hotel bookings:', data);

                        const row = document.createElement('tr');
                        row.innerHTML = `
                          <td>${booking.hotelName}</td>
                          <td>${data.firstName} ${data.lastName}</td>
                          <td>${booking.roomType}</td>
                          <td>${booking.checkinDate}</td>
                          <td>${booking.price}</td>
                          <td><span class="badge badge-success">${booking.status}</span></td>
                   
                       `;
                        hotelBookings.appendChild(row);
                    })
                    .catch(error => {
                            console.error('Error fetching client name:', error);
                            alert("Error fetching client name: " + error.message);
                   });

            });
        })
        .catch(error => {
            console.error('Error fetching reviews:', error);
            hotelBookings.innerHTML = `
            <div class="error-message">
                <p>Error loading bookings: ${error.message}</p>
                <p>Please try refreshing the page or contact support if the problem persists.</p>
            </div>`;
        });
}


// Separate function for handling hotel selection for updates
function handleHotelSelectionForUpdate(e) {
    const value = e.target.value;
    const message = document.getElementById('message');

    if (value === 'all') {

        message.innerHTML = '<p>Please select a hotel for the update</p>';
        localStorage.setItem('updatesHotelId', 'all');
        console.log('currently ', localStorage.getItem('updatesHotelId'));
        return;
    }

    message.innerHTML = '';

    console.log('Selected hotel id:', value);
    localStorage.setItem('updatesHotelId', value);
    console.log('Current hotel set to: ', localStorage.getItem('updatesHotelId'));

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


function getBookings(){
    const bookings = document.getElementById('booking-list');
    const currentUser = JSON.parse(localStorage.getItem('user'));
    const ownerId = currentUser.id;

    fetch(`/api/bookings/hotels/${encodeURIComponent(ownerId)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Success data in native form:', data);

            data.forEach(book => {

                fetch(`/api/bookings/hotels/${encodeURIComponent(book.clientId)}`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }).then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                        });
                    }
                    return response.json();
                })
                    .then(data => {
                        console.log('Hotel bookings:', data);

                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${book.clientId || data.firstName || data.lastName}</td>
                            <td>${book.hotelName}</td>
                            <td>${book.roomType}</td>
                            <td>${book.checkinDate}</td>
                            <td>$${book.price}</td>
                            <td>${book.cancellationDate}</td>
                            <td><span class="badge badge-success">${book.status}</span></td>
<!--                            <td>-->
<!--                              <button class="btn btn-secondary btn-sm">-->
<!--                                <i class="fas fa-eye"></i> Details-->
<!--                              </button>-->
<!--                            </td>-->
                            `
                        bookings.appendChild(row);
                    })
            });
        })
        .catch(error => {
            console.error('Error fetching bookings: ', error);
            bookings.innerHTML = `<p>Error fetching bookings: ${error.message}</p>`;
        })
}



function addFaq(){
    document.getElementById('add-faq-form').addEventListener('submit', function (e){
        e.preventDefault();

        const HotelFaqs = document.getElementById('faq-body');
        const hotelId = localStorage.getItem('FaqsHotelId');
        if(!hotelId || hotelId === 'all'){
            alert('Please select a hotel first before adding a FAQ!');
            return;
        }
        console.log(this);

        const formData = new FormData(this);
        const jsonData = {};

        console.log('hotel: ', hotelId);

        formData.forEach((value, key) => {
                jsonData[key] = value;
                console.log(`${key}: ${value}`);
        });
        console.log('Final jsonData:', jsonData);

        fetch(`/api/${encodeURIComponent(hotelId)}/faqs/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(jsonData),
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
                console.log('FAQ added successfully:', data);

                // Reset form
                document.getElementById('add-faq-form').reset();

                // Show success message
                alert('FAQ Added successfully!');

                // Refresh the room list
                const selectedHotel = document.getElementById('hotel-faq-filter');
                if (selectedHotel) {
                    selectedHotel.dispatchEvent(new Event('change'));
                }

            })
            .catch(error => {
                console.error('Error adding FAQ:', error);
                alert(`Failed to add FAQ: ${error.message}`);
            })
    });

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
            alert('Room added successfully! ');
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
    localStorage.setItem('FaqsHotelId', 'all');
    localStorage.setItem('QuestionsHotelId', 'all');
    localStorage.setItem('ReviewsHotelId', 'none');

    const filterHotelFAQ = document.getElementById('hotel-faq-filter');

    const inputFaqQuestion = document.getElementById('faq-question');
    const inputFaqAnswer = document.getElementById('faq-answer');

    filterHotelFAQ.addEventListener('change', function() {
        if (filterHotelFAQ.value === 'all') {
            inputFaqQuestion.disabled = true;
            inputFaqAnswer.disabled = true;
            alert('Choose a hotel to add a FAQ');
        } else {
            inputFaqAnswer.disabled = false;
            inputFaqAnswer.value = '';
            inputFaqQuestion.disabled = false;
            inputFaqAnswer.value = '';
        }
    });

    getServices();
    getBookings();
    registerHotels();
    getOwnerHotels();
    updateHotel();
    addRoomType();
    addFaq();

    // Add event listener for the add room button
    const addRoomButton = document.getElementById('add-room-button');
    if (addRoomButton) {
        addRoomButton.addEventListener('click', handleAddRoomButtonClick);
    }
});
