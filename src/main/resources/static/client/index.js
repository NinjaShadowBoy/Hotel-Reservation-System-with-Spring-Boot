$(document).ready(function () {
    // Global variables
    let hotels = [];
    let services = [];
    let reservations = [];
    let isLoggedIn = false;
    let currentUser = null;
    const itemsPerPage = 5;
    let currentPage = 1;

    // Demo user data for testing
    const demoUsers = [
        {id: 1, email: "john", password: "123", firstName: "John", lastName: "Doe"},
        {id: 2, email: "jane", password: "123", firstName: "Jane", lastName: "Smith"}
    ];

    // Initialize the page
    init();

    function init() {
        // Check if user is already logged in (from localStorage in this demo)
        checkLoginStatus();

        // Load services for filter options
        loadServices();

        // Load hotels
        loadHotels();

        // If logged in, load reservations
        if (isLoggedIn) {
            loadReservations();
        }

        // Setup event listeners
        setupEventListeners();
    }

    function checkLoginStatus() {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            currentUser = JSON.parse(savedUser);
            isLoggedIn = true;
            updateUIForLoggedInUser();
        }
    }

    function updateUIForLoggedInUser() {
        $('#authButtons').addClass('hidden');
        $('#userProfile').removeClass('hidden');
        $('#welcomeUser').text(`Welcome, ${currentUser.firstName}`);

        $('#loginMessage').addClass('hidden');
        $('#reservationsLoader').removeClass('hidden');

        // Load reservations for the logged-in user
        loadReservations();
    }

    function updateUIForLoggedOutUser() {
        $('#authButtons').removeClass('hidden');
        $('#userProfile').addClass('hidden');

        $('#reservationCards').addClass('hidden');
        $('#loginMessage').removeClass('hidden');
        $('#reservationsLoader').addClass('hidden');
    }

    function loadServices() {
        $('#amenitiesLoader').removeClass('hidden');

        // In a real app, this would be an AJAX call to the backend
        // GET /api/services
        setTimeout(() => {
            // Mock data for services
            services = [
                'WiFi', 'Breakfast', 'Spa Access', 'Fitness Center',
                'Swimming Pool', 'Room Service', 'Concierge', 'Parking',
                'Business Center', 'Airport Shuttle', 'Laundry Service',
                'Mini Bar', 'Pet Friendly', 'Child Care', 'Restaurant Discount'
            ];

            fetch("/api/services").then(response => response.json())
                .then(data => {
                    services = data;
                    console.log("Services loaded ", services);
                    renderServiceFilters();
                    $('#amenitiesLoader').addClass('hidden');
                    return data;
                });

        }, 500);
    }

    function renderServiceFilters() {
        const filtersContainer = $('#amenitiesFilters');
        filtersContainer.empty(); // Clear any existing content before adding filters

        services.forEach(service => {
            service = service.toString().split(':')
            console.log(service)
            const iconClass = service[1]
            service = service[0]
            const serviceId = service.toLowerCase().replace(/\s+/g, '-');

            const checkboxItem = $(`
                <div class="checkbox-item">
                    <input type="checkbox" id="${serviceId}" name="amenity" value="${service}">
                    <label for="${serviceId}"><i class="${iconClass}"></i>  ${service}</label>
                </div>
            `);

            filtersContainer.append(checkboxItem);
        });

        // Add event listener for filter changes
        $('input[name="amenity"]').on('change', function () {
            filterHotels();
        });
    }

    function loadHotels() {
        $('#hotelsLoader').removeClass('hidden');
        $('#hotelCards .hotel-card').remove(); // Remove only hotel cards, keep loader

        // In a real app, this would be an AJAX call to the backend
        // GET /api/hotels
        setTimeout(() => {
            // Mock data for hotels based on your database structure
            hotels = [
                {
                    id: 1,
                    name: 'Luxury Grand Hotel',
                    image: '/api/placeholder/800/600',
                    location: 'New York, NY',
                    desc: 'Luxury Hotel offers an unparalleled experience with panoramic city views. Our elegant rooms are designed for comfort and style, featuring premium amenities and modern conveniences.',
                    rating: 4.8,
                    services: ['WiFi', 'Breakfast', 'Spa Access', 'Room Service', 'Concierge'],
                    lowestPrice: 150
                },
                {
                    id: 2,
                    name: 'Seaside Resort & Spa',
                    image: '/api/placeholder/800/600',
                    location: 'Miami Beach, FL',
                    desc: 'Seaside Resort & Spa is a beachfront paradise where relaxation meets luxury. Wake up to stunning ocean views from your private balcony.',
                    rating: 4.7,
                    services: ['WiFi', 'Breakfast', 'Swimming Pool', 'Spa Access', 'Room Service', 'Child Care'],
                    lowestPrice: 300
                },
                {
                    id: 3,
                    name: 'Mountain Retreat Lodge',
                    image: '/api/placeholder/800/600',
                    location: 'Aspen, CO',
                    desc: 'Mountain Retreat Lodge is nestled among towering pines with breathtaking views of snow-capped peaks. Our rustic-chic accommodations blend natural elements with modern luxury.',
                    rating: 4.9,
                    services: ['WiFi', 'Breakfast', 'Parking', 'Fitness Center', 'Pet Friendly', 'Restaurant Discount'],
                    lowestPrice: 280
                },
                {
                    id: 4,
                    name: 'City Center Suites',
                    image: '/api/placeholder/800/600',
                    location: 'Chicago, IL',
                    desc: 'City Center Suites offers sophisticated accommodations for business and leisure travelers alike. Our spacious suite-style rooms include fully equipped kitchenettes.',
                    rating: 4.6,
                    services: ['WiFi', 'Breakfast', 'Business Center', 'Fitness Center', 'Laundry Service', 'Room Service'],
                    lowestPrice: 200
                },
                {
                    id: 5,
                    name: 'Vintage Boutique Inn',
                    image: '/api/placeholder/800/600',
                    location: 'Charleston, SC',
                    desc: 'Vintage Boutique Inn is housed in a meticulously restored 19th-century mansion, offering a charming blend of historic character and modern comforts.',
                    rating: 4.9,
                    services: ['WiFi', 'Breakfast', 'Room Service', 'Concierge', 'Restaurant Discount'],
                    lowestPrice: 220
                }
            ];

            fetch("/api/hotels").then(response => response.json())
                .then(data => {
                    hotels = data;
                    console.log("Hotels loaded ", hotels);
                    renderHotels(hotels);
                    renderPagination(hotels.length);
                    $('#hotelsLoader').addClass('hidden');
                    return data;
                });

        }, 800);
    }

    function renderHotels(hotelsToRender) {
        const hotelCardsContainer = $('#hotelCards');
        hotelCardsContainer.empty();

        if (hotelsToRender.length === 0) {
            hotelCardsContainer.html('<p class="no-reservations">No hotels found matching your criteria.</p>');
            return;
        }

        // Calculate start and end indices for pagination
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, hotelsToRender.length);
        const paginatedHotels = hotelsToRender.slice(startIndex, endIndex);

        paginatedHotels.forEach(hotel => {
            const hotelCard = $(`
                <div class="hotel-card" data-hotel-id="${hotel.id}">
                    <img src="${hotel.image}" alt="${hotel.name}" class="hotel-image">
                    <div class="hotel-details">
                        <div class="hotel-header">
                            <h2 class="hotel-name">${hotel.name}</h2>
                            <div class="hotel-rating">
                                <span class="hotel-star"><i class="fas fa-star"></i></span>
                                <span>${hotel.rating.toFixed(1)}</span>
                            </div>
                        </div>
                        <div class="hotel-location">
                            <i class="fas fa-map-marker-alt"></i>
                            <span>${hotel.location}</span>
                        </div>
                        <p class="hotel-description">${hotel.desc}</p>
                        <div class="hotel-services">
                            ${hotel.services.slice(0).map(service => {
                    service = service.toString().split(':')
                    const iconClass = service[1]
                    service = service[0];

                    return `<span class="service-tag"><i class="${iconClass}"></i>  ${service}</span>`
                }
            ).join('')}
                        </div>
                        <div class="hotel-footer">
                            <div class="hotel-price">
                                From $${hotel.lowestPrice}<span class="price-period">/night</span>
                            </div>
                            <button class="btn btn-primary view-details-btn" data-hotel-id="${hotel.id}">View Details</button>
                        </div>
                    </div>
                </div>
            `);

            hotelCardsContainer.append(hotelCard);
        });

        // Add event listener for view details buttons
        $('.view-details-btn').on('click', function () {
            const hotelId = $(this).data('hotel-id');
            redirectToHotelDetails(hotelId);
        });

        // Make the entire hotel card clickable
        $('.hotel-card').on('click', function (e) {
            // Prevent triggering if clicking on the button
            if (!$(e.target).closest('.view-details-btn').length) {
                const hotelId = $(this).data('hotel-id');
                redirectToHotelDetails(hotelId);
            }
        });
    }

    function renderPagination(totalItems) {
        const paginationContainer = $('#pagination');
        paginationContainer.empty();

        const totalPages = Math.ceil(totalItems / itemsPerPage);

        if (totalPages <= 1) {
            return;
        }

        // Previous button
        if (currentPage > 1) {
            paginationContainer.append(`
                <div class="page-item" data-page="${currentPage - 1}">
                    <i class="fas fa-angle-left"></i>
                </div>
            `);
        }

        // Page numbers
        for (let i = 1; i <= totalPages; i++) {
            paginationContainer.append(`
                <div class="page-item ${i === currentPage ? 'active' : ''}" data-page="${i}">
                    ${i}
                </div>
            `);
        }

        // Next button
        if (currentPage < totalPages) {
            paginationContainer.append(`
                <div class="page-item" data-page="${currentPage + 1}">
                    <i class="fas fa-angle-right"></i>
                </div>
            `);
        }

        // Add event listener for pagination
        $('.page-item').on('click', function () {
            currentPage = parseInt($(this).data('page'));
            filterHotels();
        });
    }

    function loadReservations() {
        if (!isLoggedIn) return;

        $('#reservationsLoader').removeClass('hidden');
        $('#reservationCards').addClass('hidden');

        // In a real app, this would be an AJAX call to the backend
        // GET /api/reservations/{userId}
        setTimeout(() => {
            // Mock data for reservations
            reservations = [
                {
                    id: 1,
                    hotelName: 'Luxury Grand Hotel',
                    roomType: 'Executive Suite',
                    price: 450,
                    date: '2025-05-10',
                    checkinDate: '2025-06-15',
                    cancelable: true
                },
                {
                    id: 2,
                    hotelName: 'Seaside Resort & Spa',
                    roomType: 'Beach Front Suite',
                    price: 500,
                    date: '2025-04-25',
                    checkinDate: '2025-07-03',
                    cancelable: true
                },
                {
                    id: 3,
                    hotelName: 'Mountain Retreat Lodge',
                    roomType: 'Premium Lodge',
                    price: 650,
                    date: '2025-03-10',
                    checkinDate: '2025-05-20',
                    cancelable: false
                }
            ];

            fetch(`/api/bookings/${currentUser.id}`).then(response => response.json())
                .then(data => {
                    reservations = data;
                    console.log("Reservations loaded", reservations);
                    renderReservations();
                    $('#reservationsLoader').addClass('hidden');
                    $('#reservationCards').removeClass('hidden');
                    return data;
                });

        }, 600);
    }

    function renderReservations() {
        const reservationCardsContainer = $('#reservationCards');
        reservationCardsContainer.empty();

        if (reservations.length === 0) {
            reservationCardsContainer.html('<p class="no-reservations">You have no reservations yet.</p>');
            return;
        }

        reservations.forEach(reservation => {
            const reservationCard = $(`
                <div class="reservation-card" data-reservation-id="${reservation.id}">
                    <h3 class="reservation-hotel">${reservation.hotelName}</h3>
                    <p class="reservation-details">${reservation.roomType}</p>
                    <p class="reservation-date">Check-in: ${formatDate(reservation.checkinDate)}</p>
                    <p class="reservation-price">$${reservation.price}</p>
                    ${reservation.cancelable ?
                `<button class="cancel-button" data-reservation-id="${reservation.id}">Cancel Reservation</button>` :
                ''}
                </div>
            `);

            reservationCardsContainer.append(reservationCard);
        });

        // Add event listener for cancel buttons
        $('.cancel-button').on('click', function () {
            const reservationId = $(this).data('reservation-id');
            cancelReservation(reservationId);
        });
    }

    function filterHotels() {
        const selectedAmenities = $('input[name="amenity"]:checked').map(function () {
            return $(this).val();
        }).get();

        const searchTerm = $('#hotelSearch').val().toLowerCase();

        let filteredHotels = hotels;

        // Filter by search term
        if (searchTerm) {
            filteredHotels = filteredHotels.filter(hotel =>
                hotel.name.toLowerCase().includes(searchTerm) ||
                hotel.location.toLowerCase().includes(searchTerm)
            );
        }

        // Filter by selected amenities
        if (selectedAmenities.length > 0) {
            filteredHotels = filteredHotels.filter(hotel =>
                selectedAmenities.every(amenity => hotel.services.map(
                    service => service.split(":")[0]
                ).includes(amenity))
            );
        }

        renderHotels(filteredHotels);
        renderPagination(filteredHotels.length);
    }

    function setupEventListeners() {
        // Search button click
        $('#searchBtn').on('click', function () {
            filterHotels();
        });

        // Enter key on search input
        $('#hotelSearch').on('keyup', function (e) {
            if (e.key === 'Enter') {
                filterHotels();
            }
        });

        // Login button click
        $('#loginBtn, #sidebarLoginBtn').on('click', function () {
            showLoginModal();
        });

        // Sign up button click
        $('#signupBtn').on('click', function () {
            showSignupModal();
        });

        // Logout button click
        $('#logoutBtn').on('click', function () {
            logout();
        });
    }

    function redirectToHotelDetails(hotelId) {
        // In a real app, this would redirect to a new page
        window.location.href = `/client/reservation/${hotelId}`;
        console.log(`Redirecting to hotel details page for hotel ID: ${hotelId}`);
    }

    function cancelReservation(reservationId) {
        // In a real app, this would be an AJAX call to the backend
        // DELETE /api/reservations/{reservationId}

        // Show loading or disable button
        const cancelButton = $(`.cancel-button[data-reservation-id="${reservationId}"]`)
            .text('Cancelling...')
            .prop('disabled', true);

        setTimeout(() => {
            // Remove the reservation from the list
            reservations = reservations.filter(res => res.id !== reservationId);

            // Re-render reservations
            renderReservations();

            // Show success message
            alert('Reservation cancelled successfully!');
        }, 500);
    }

    function formatDate(dateString) {
        const options = {year: 'numeric', month: 'long', day: 'numeric'};
        return new Date(dateString).toLocaleDateString(undefined, options);
    }

    // Login/Signup Modal functions
    function showLoginModal() {
        // In a real app, this would show a proper modal
        const email = prompt('Please enter your email:');
        const password = prompt('Please enter your password:');

        if (!email || !password) return;

        login(email, password);
    }

    function showSignupModal() {
        // In a real app, this would show a proper modal
        const firstName = prompt('Please enter your first name:');
        const lastName = prompt('Please enter your last name:');
        const email = prompt('Please enter your email:');
        const password = prompt('Please enter your password:');

        if (!firstName || !lastName || !email || !password) return;

        signup(firstName, lastName, email, password);
    }

    function login(email, password) {
        // In a real app, this would be an AJAX call to the backend
        // POST /api/login

        // Check credentials against demo users
        const user = demoUsers.find(u =>
            u.email === email && u.password === password
        );

        if (user) {
            currentUser = user;
            isLoggedIn = true;

            // Save to localStorage
            localStorage.setItem('currentUser', JSON.stringify(user));

            // Update UI
            updateUIForLoggedInUser();

            // Load reservations
            loadReservations();

            alert(`Welcome back, ${user.firstName}!`);
        } else {
            alert('Invalid credentials. Please try again.');
        }
    }

    function signup(firstName, lastName, email, password) {
        // In a real app, this would be an AJAX call to the backend
        // POST /api/users

        // Check if email already exists
        if (demoUsers.some(u => u.email === email)) {
            alert('Email already in use. Please try another one.');
            return;
        }

        // Create new user
        const newUser = {
            id: demoUsers.length + 1,
            firstName,
            lastName,
            email,
            password
        };

        // Add to demo users
        demoUsers.push(newUser);

        // Log in the new user
        currentUser = newUser;
        isLoggedIn = true;

        // Save to localStorage
        localStorage.setItem('currentUser', JSON.stringify(newUser));

        // Update UI
        updateUIForLoggedInUser();

        alert(`Welcome, ${firstName}! Your account has been created.`);
    }

    function logout() {
        // Clear user data
        currentUser = null;
        isLoggedIn = false;
        reservations = [];

        // Remove from localStorage
        localStorage.removeItem('currentUser');

        // Update UI
        updateUIForLoggedOutUser();

        alert('You have been logged out successfully.');
    }
});