/**
 * Hotel Browsing System - Main JavaScript
 * 
 * This file handles the functionality of the hotel browsing page, including:
 * - Loading and displaying hotels
 * - Filtering hotels by amenities and search terms
 * - User authentication (login/signup)
 * - Managing user reservations
 */

// Use IIFE to avoid polluting global namespace
(function() {
    'use strict';

    // App configuration
    const config = {
        itemsPerPage: 5,
        apiEndpoints: {
            hotels: '/api/hotels',
            services: '/api/services',
            bookings: '/api/bookings'
        },
        mockDelays: {
            services: 500,
            hotels: 800,
            reservations: 600
        }
    };

    // State management
    const state = {
        hotels: [],
        services: [],
        reservations: [],
        isLoggedIn: false,
        currentUser: null,
        currentPage: 1,
        // Demo user data for testing
        demoUsers: [
            {id: 1, email: "john", password: "123", firstName: "John", lastName: "Doe"},
            {id: 2, email: "jane", password: "123", firstName: "Jane", lastName: "Smith"}
        ]
    };

    // DOM elements cache
    const DOM = {
        auth: {
            authButtons: $('#authButtons'),
            userProfile: $('#userProfile'),
            welcomeUser: $('#welcomeUser'),
            loginBtn: $('#loginBtn'),
            sidebarLoginBtn: $('#sidebarLoginBtn'),
            signupBtn: $('#signupBtn'),
            logoutBtn: $('#logoutBtn')
        },
        hotels: {
            hotelCards: $('#hotelCards'),
            hotelsLoader: $('#hotelsLoader'),
            searchInput: $('#hotelSearch'),
            searchBtn: $('#searchBtn')
        },
        filters: {
            amenitiesFilters: $('#amenitiesFilters'),
            amenitiesLoader: $('#amenitiesLoader')
        },
        reservations: {
            reservationCards: $('#reservationCards'),
            reservationsLoader: $('#reservationsLoader'),
            loginMessage: $('#loginMessage')
        },
        pagination: {
            container: $('#pagination')
        },
        modals: {
            login: {
                modal: $('#loginModal'),
                closeBtn: $('#closeLoginModal'),
                form: $('#loginForm'),
                emailInput: $('#loginEmail'),
                passwordInput: $('#loginPassword'),
                switchToSignup: $('#switchToSignup')
            },
            signup: {
                modal: $('#signupModal'),
                closeBtn: $('#closeSignupModal'),
                form: $('#signupForm'),
                firstNameInput: $('#signupFirstName'),
                lastNameInput: $('#signupLastName'),
                emailInput: $('#signupEmail'),
                passwordInput: $('#signupPassword'),
                switchToLogin: $('#switchToLogin')
            }
        }
    };

    /**
     * Initialize the page
     */
    function init() {
        // Check if user is already logged in (from localStorage in this demo)
        checkLoginStatus();

        // Load services for filter options
        loadServices();

        // Load hotels
        loadHotels();

        // If logged in, load reservations
        if (state.isLoggedIn) {
            loadReservations();
        }

        // Setup event listeners
        setupEventListeners();
    }

    /**
     * Check if user is already logged in
     */
    function checkLoginStatus() {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            state.currentUser = JSON.parse(savedUser);
            state.isLoggedIn = true;
            updateUIForLoggedInUser();
        }
    }

    /**
     * Update UI elements for logged in user
     */
    function updateUIForLoggedInUser() {
        DOM.auth.authButtons.addClass('hidden');
        DOM.auth.userProfile.removeClass('hidden');
        DOM.auth.welcomeUser.text(`Welcome, ${state.currentUser.firstName}`);

        DOM.reservations.loginMessage.addClass('hidden');
        DOM.reservations.reservationsLoader.removeClass('hidden');

        // Load reservations for the logged-in user
        loadReservations();
    }

    /**
     * Update UI elements for logged out user
     */
    function updateUIForLoggedOutUser() {
        DOM.auth.authButtons.removeClass('hidden');
        DOM.auth.userProfile.addClass('hidden');

        DOM.reservations.reservationCards.addClass('hidden');
        DOM.reservations.loginMessage.removeClass('hidden');
        DOM.reservations.reservationsLoader.addClass('hidden');
    }

    /**
     * Load available services for filter options
     */
    function loadServices() {
        DOM.filters.amenitiesLoader.removeClass('hidden');

        // In a real app, this would be an AJAX call to the backend
        // GET /api/services
        setTimeout(() => {
            // Mock data for services
            state.services = [
                'WiFi', 'Breakfast', 'Spa Access', 'Fitness Center',
                'Swimming Pool', 'Room Service', 'Concierge', 'Parking',
                'Business Center', 'Airport Shuttle', 'Laundry Service',
                'Mini Bar', 'Pet Friendly', 'Child Care', 'Restaurant Discount'
            ];

            fetch(config.apiEndpoints.services)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to load services');
                    }
                    return response.json();
                })
                .then(data => {
                    state.services = data;
                    console.log("Services loaded ", state.services);
                    renderServiceFilters(state.services);
                    DOM.filters.amenitiesLoader.addClass('hidden');
                })
                .catch(error => {
                    console.error('Error loading services:', error);
                    DOM.filters.amenitiesLoader.addClass('hidden');
                });

        }, 500);
    }

    function renderServiceFilters(services) {
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

    /**
     * Load hotels from API
     */
    function loadHotels() {
        DOM.hotels.hotelsLoader.removeClass('hidden');
        DOM.hotels.hotelCards.find('.hotel-card').remove(); // Remove only hotel cards, keep loader

        // In a real app, this would be an AJAX call to the backend
        // GET /api/hotels
        setTimeout(() => {
            // Mock data for hotels based on your database structure
            state.hotels = [
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

            fetch(config.apiEndpoints.hotels)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to load hotels');
                    }
                    return response.json();
                })
                .then(data => {
                    state.hotels = data;
                    console.log("Hotels loaded ", state.hotels);
                    renderHotels(state.hotels);
                    renderPagination(state.hotels.length);
                    DOM.hotels.hotelsLoader.addClass('hidden');
                })
                .catch(error => {
                    console.error('Error loading hotels:', error);
                    // Fallback to mock data if API fails
                    renderHotels([]);
                    renderPagination(state.hotels.length);
                    DOM.hotels.hotelsLoader.addClass('hidden');
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
        const startIndex = (state.currentPage - 1) * config.itemsPerPage;
        const endIndex = Math.min(startIndex + config.itemsPerPage, hotelsToRender.length);
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

    /**
     * Render pagination controls
     * @param {number} totalItems - Total number of items to paginate
     */
    function renderPagination(totalItems) {
        const paginationContainer = DOM.pagination.container;
        paginationContainer.empty();

        const totalPages = Math.ceil(totalItems / config.itemsPerPage);

        if (totalPages <= 1) {
            return;
        }

        // Previous button
        if (state.currentPage > 1) {
            paginationContainer.append(`
                <div class="page-item" data-page="${state.currentPage - 1}">
                    <i class="fas fa-angle-left"></i>
                </div>
            `);
        }

        // Page numbers
        for (let i = 1; i <= totalPages; i++) {
            paginationContainer.append(`
                <div class="page-item ${i === state.currentPage ? 'active' : ''}" data-page="${i}">
                    ${i}
                </div>
            `);
        }

        // Next button
        if (state.currentPage < totalPages) {
            paginationContainer.append(`
                <div class="page-item" data-page="${state.currentPage + 1}">
                    <i class="fas fa-angle-right"></i>
                </div>
            `);
        }

        // Add event listener for pagination
        $('.page-item').on('click', function () {
            state.currentPage = parseInt($(this).data('page'));
            filterHotels();
        });
    }

    /**
     * Load user reservations from API
     */
    function loadReservations() {
        if (!state.isLoggedIn) return;

        DOM.reservations.reservationsLoader.removeClass('hidden');
        DOM.reservations.reservationCards.addClass('hidden');

        // In a real app, this would be an AJAX call to the backend
        // GET /api/reservations/{userId}
        setTimeout(() => {
            // Mock data for reservations
            // state.reservations = [
            //     {
            //         id: 1,
            //         hotelName: 'Luxury Grand Hotel',
            //         roomType: 'Executive Suite',
            //         price: 450,
            //         date: '2025-05-10',
            //         checkinDate: '2025-06-15',
            //         cancelable: true
            //     },
            //     {
            //         id: 2,
            //         hotelName: 'Seaside Resort & Spa',
            //         roomType: 'Beach Front Suite',
            //         price: 500,
            //         date: '2025-04-25',
            //         checkinDate: '2025-07-03',
            //         cancelable: true
            //     },
            //     {
            //         id: 3,
            //         hotelName: 'Mountain Retreat Lodge',
            //         roomType: 'Premium Lodge',
            //         price: 650,
            //         date: '2025-03-10',
            //         checkinDate: '2025-05-20',
            //         cancelable: false
            //     }
            // ];

            $.ajax({
                url: `${config.apiEndpoints.bookings}/${state.currentUser.id}`,
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    state.reservations = data;
                    console.log("Reservations loaded", state.reservations);
                    renderReservations(state.reservations);
                    DOM.reservations.reservationsLoader.addClass('hidden');
                    DOM.reservations.reservationCards.removeClass('hidden');
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error loading reservations:', textStatus, errorThrown);
                    // Fallback to mock data if API fails
                    renderReservations();
                    DOM.reservations.reservationsLoader.addClass('hidden');
                    DOM.reservations.reservationCards.removeClass('hidden');
                }
            });
        }, 600);
    }

    function renderReservations(reservations) {
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

        let filteredHotels = state.hotels;

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

        // Modal close buttons
        $('#closeLoginModal').on('click', function() {
            $('#loginModal').addClass('hidden');
        });

        $('#closeSignupModal').on('click', function() {
            $('#signupModal').addClass('hidden');
        });

        // Switch between login and signup modals
        $('#switchToSignup').on('click', function(e) {
            e.preventDefault();
            $('#loginModal').addClass('hidden');
            $('#signupModal').removeClass('hidden');
        });

        $('#switchToLogin').on('click', function(e) {
            e.preventDefault();
            $('#signupModal').addClass('hidden');
            $('#loginModal').removeClass('hidden');
        });

        // Form submissions
        $('#loginForm').on('submit', function(e) {
            e.preventDefault();
            const email = $('#loginEmail').val();
            const password = $('#loginPassword').val();

            if (!email || !password) return;

            login(email, password);
            $('#loginModal').addClass('hidden');
        });

        $('#signupForm').on('submit', function(e) {
            e.preventDefault();
            const firstName = $('#signupFirstName').val();
            const lastName = $('#signupLastName').val();
            const email = $('#signupEmail').val();
            const password = $('#signupPassword').val();

            if (!firstName || !lastName || !email || !password) return;

            signup(firstName, lastName, email, password);
            $('#signupModal').addClass('hidden');
        });
    }

    function redirectToHotelDetails(hotelId) {
        // In a real app, this would redirect to a new page
        window.location.href = `/client/reservation/${hotelId}`;
        console.log(`Redirecting to hotel details page for hotel ID: ${hotelId}`);
    }

    /**
     * Cancel a reservation
     * @param {number} reservationId - ID of the reservation to cancel
     */
    function cancelReservation(reservationId) {
        // In a real app, this would be an AJAX call to the backend
        // DELETE /api/reservations/{reservationId}

        // Show loading or disable button
        const cancelButton = $(`.cancel-button[data-reservation-id="${reservationId}"]`)
            .text('Cancelling...')
            .prop('disabled', true);

        setTimeout(() => {
            // Remove the reservation from the list
            state.reservations = state.reservations.filter(res => res.id !== reservationId);

            // Re-render reservations
            renderReservations(state.reservations);

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
        // Clear previous inputs
        $('#loginEmail').val('');
        $('#loginPassword').val('');

        // Show the modal
        $('#loginModal').removeClass('hidden');
    }

    function showSignupModal() {
        // Clear previous inputs
        $('#signupFirstName').val('');
        $('#signupLastName').val('');
        $('#signupEmail').val('');
        $('#signupPassword').val('');

        // Show the modal
        $('#signupModal').removeClass('hidden');
    }

    /**
     * Log in a user
     * @param {string} email - User email
     * @param {string} password - User password
     */
    function login(email, password) {
        // Make an API call to authenticate the user

        $.ajax({
            url: `/api/login`,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({username: email, password: password}),
            success: (user) => {
                localStorage.setItem('currentUser', JSON.stringify(user));
                state.currentUser = user;
                state.isLoggedIn = true;

                updateUIForLoggedInUser();
                alert('Login successful!');
            },
            error: (xhr, status, error) => {
                if (xhr.status === 500) {
                    console.error("Server Error:", xhr.responseText);
                } else if (xhr.status === 401 || xhr.status === 400) {
                    alert('Wrong credentials. Please try again');
                    console.error("Unhandled Error:", xhr.status, xhr.responseText);
                }

                alert('Failed to submit review. Please try again.');
                console.error('Review submission error:', xhr.responseText);
            }
        });
    }

    /**
     * Sign up a new user
     * @param {string} firstName - User first name
     * @param {string} lastName - User last name
     * @param {string} email - User email
     * @param {string} password - User password
     */
    function signup(firstName, lastName, email, password) {
        // In a real application, this would make an API call to register the user
        // For now, we'll simulate a successful signup

        $.ajax({
            url: `/api/register`,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password
            }),
            success: (user) => {
                localStorage.setItem('currentUser', JSON.stringify(user));
                state.currentUser = user;
                state.isLoggedIn = true;

                updateUIForLoggedInUser();
                alert('SignUp & Login successful!');
            },
            error: (xhr, status, error) => {
                if (xhr.status === 500) {
                    console.error("Server Error:", xhr.responseText);
                } else if (xhr.status === 401 || xhr.status === 400) {
                    console.error("Unhandled Error:", xhr.status, xhr.responseText);
                }

                alert('Error creating your account. Please try again');
            }
        });
    }


    /**
     * Log out the current user
     */
    function logout() {
        // Clear user data
        state.currentUser = null;
        state.isLoggedIn = false;
        state.reservations = [];

        // Remove from localStorage
        localStorage.removeItem('currentUser');

        // Update UI
        updateUIForLoggedOutUser();

        alert('You have been logged out successfully.');
    }

    // Initialize the page
    init()
})();
