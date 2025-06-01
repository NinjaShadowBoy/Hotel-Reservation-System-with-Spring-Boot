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
            services: 2000,
            hotels: 2200,
            reservations: 2000
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

    // DOM elements cache - using function-based style to avoid stale references
    const DOM = {
        auth: {
            authButtons: () => $('#authButtons'),
            userProfile: () => $('#userProfile'),
            welcomeUser: () => $('#welcomeUser'),
            loginBtn: () => $('#loginBtn'),
            sidebarLoginBtn: () => $('#sidebarLoginBtn'),
            signupBtn: () => $('#signupBtn'),
            logoutBtn: () => $('#logoutBtn')
        },
        hotels: {
            hotelCards: () => $('#hotelCards'),
            hotelsLoader: () => $('#hotelsLoader'),
            searchInput: () => $('#hotelSearch'),
            searchBtn: () => $('#searchBtn')
        },
        filters: {
            amenitiesFilters: () => $('#amenitiesFilters'),
            amenitiesLoader: () => $('#amenitiesLoader')
        },
        reservations: {
            reservationCards: () => $('#reservationCards'),
            reservationsLoader: () => $('#reservationsLoader'),
            loginMessage: () => $('#loginMessage')
        },
        pagination: {
            container: () => $('#pagination')
        },
        modals: {
            login: {
                modal: () => $('#loginModal'),
                closeBtn: () => $('#closeLoginModal'),
                form: () => $('#loginForm'),
                emailInput: () => $('#loginEmail'),
                passwordInput: () => $('#loginPassword'),
                switchToSignup: () => $('#switchToSignup')
            },
            signup: {
                modal: () => $('#signupModal'),
                closeBtn: () => $('#closeSignupModal'),
                form: () => $('#signupForm'),
                firstNameInput: () => $('#signupFirstName'),
                lastNameInput: () => $('#signupLastName'),
                emailInput: () => $('#signupEmail'),
                passwordInput: () => $('#signupPassword'),
                switchToLogin: () => $('#switchToLogin')
            },
            notification: {
                modal: () => $('#notificationModal'),
                closeBtn: () => $('#closeNotificationModal'),
                okBtn: () => $('#notificationOkBtn')
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
        DOM.auth.authButtons().addClass('hidden');
        DOM.auth.userProfile().removeClass('hidden');
        DOM.auth.welcomeUser().text(`Welcome, ${state.currentUser.firstName}`);

        DOM.reservations.loginMessage().addClass('hidden');
        DOM.reservations.reservationsLoader().removeClass('hidden');

        // Load reservations for the logged-in user
        loadReservations();
    }

    /**
     * Update UI elements for logged out user
     */
    function updateUIForLoggedOutUser() {
        DOM.auth.authButtons().removeClass('hidden');
        DOM.auth.userProfile().addClass('hidden');

        DOM.reservations.reservationCards().addClass('hidden');
        DOM.reservations.loginMessage().removeClass('hidden');
        DOM.reservations.reservationsLoader().addClass('hidden');
    }

    /**
     * Load available services for filter options
     */
    function loadServices() {
        DOM.filters.amenitiesLoader().removeClass('hidden');

        // Fallback mock data in case the API fails
        const fallbackServices = [
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
                DOM.filters.amenitiesLoader().addClass('hidden');
                state.services = data;
                renderServiceFilters(state.services);
            })
            .catch(error => {
                DOM.filters.amenitiesLoader().addClass('hidden');
                console.error('Error loading services:', error);
                // Use fallback data if API fails
                state.services = fallbackServices;
                renderServiceFilters(state.services);
            });
    }

    function renderServiceFilters(services) {
        const filtersContainer = DOM.filters.amenitiesFilters();
        filtersContainer.empty(); // Clear any existing content before adding filters

        // Build HTML string for better performance
        let filtersHtml = '';

        services.forEach(service => {
            const parts = service.toString().split(':');
            const serviceName = parts[0];
            const iconClass = parts[1] || 'fas fa-check'; // Default icon if none provided
            const serviceId = `amenity-${parts[2] || serviceName.toLowerCase().replace(/\s+/g, '-')}`;

            filtersHtml += `
                <div class="checkbox-item">
                    <input type="checkbox" id="${serviceId}" name="amenity" value="${serviceName}">
                    <label for="${serviceId}"><i class="${iconClass}"></i>  ${serviceName}</label>
                </div>
            `;
        });

        // Set HTML content once (more efficient than multiple appends)
        filtersContainer.html(filtersHtml);

        // Use event delegation for better performance
        filtersContainer.off('change', 'input[name="amenity"]').on('change', 'input[name="amenity"]', filterHotels);
    }

    /**
     * Load hotels from API
     */
    function loadHotels() {
        DOM.hotels.hotelsLoader().removeClass('hidden');
        DOM.hotels.hotelCards().find('.hotel-card').remove(); // Remove only hotel cards, keep loader

        // Fallback mock data in case the API fails
        const fallbackHotels = [
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
                DOM.hotels.hotelsLoader().addClass('hidden');
                state.hotels = data;
                renderHotels(state.hotels);
                renderPagination(state.hotels.length);
            })
            .catch(error => {
                DOM.hotels.hotelsLoader().addClass('hidden');
                console.error('Error loading hotels:', error);
                // Fallback to mock data if API fails
                state.hotels = fallbackHotels;
                renderHotels(state.hotels);
                renderPagination(state.hotels.length);
            });
    }

    function renderHotels(hotelsToRender) {
        const hotelCardsContainer = DOM.hotels.hotelCards();
        hotelCardsContainer.empty();

        if (hotelsToRender.length === 0) {
            hotelCardsContainer.html('<p class="no-reservations">No hotels found matching your criteria.</p>');
            return;
        }

        // Calculate start and end indices for pagination
        const startIndex = (state.currentPage - 1) * config.itemsPerPage;
        const endIndex = Math.min(startIndex + config.itemsPerPage, hotelsToRender.length);
        const paginatedHotels = hotelsToRender.slice(startIndex, endIndex);

        // Build HTML string for better performance
        let hotelsHtml = '';

        paginatedHotels.forEach(hotel => {
            // Process services once
            const servicesHtml = hotel.services.map(service => {
                const parts = service.toString().split(':');
                const serviceName = parts[0];
                const iconClass = parts[1] || 'fas fa-check'; // Default icon if none provided

                return `<span class="service-tag"><i class="${iconClass}"></i> ${serviceName}</span>`;
            }).join('');

            hotelsHtml += `
                <div class="hotel-card" data-hotel-id="${hotel.id}">
                    <img src="${hotel.image}" alt="${hotel.name}" class="hotel-image" loading="lazy" width="350" height="250">
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
                            ${servicesHtml}
                        </div>
                        <div class="hotel-footer">
                            <div class="hotel-price">
                                From $${hotel.lowestPrice}<span class="price-period">/night</span>
                            </div>
                            <button class="btn btn-primary view-details-btn" data-hotel-id="${hotel.id}">View Details</button>
                        </div>
                    </div>
                </div>
            `;
        });

        // Set HTML content once (more efficient than multiple appends)
        hotelCardsContainer.html(hotelsHtml);

        // Use event delegation for better performance
        // Handle view details button clicks
        hotelCardsContainer.off('click', '.view-details-btn').on('click', '.view-details-btn', function(e) {
            e.stopPropagation(); // Prevent the hotel card click from triggering
            const hotelId = $(this).data('hotel-id');
            redirectToHotelDetails(hotelId);
        });

        // Make the entire hotel card clickable
        hotelCardsContainer.off('click', '.hotel-card').on('click', '.hotel-card', function(e) {
            // Only trigger if not clicking on the button (handled above)
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
        const paginationContainer = DOM.pagination.container();
        paginationContainer.empty();

        const totalPages = Math.ceil(totalItems / config.itemsPerPage);

        if (totalPages <= 1) {
            return;
        }

        // Build HTML string for better performance
        let paginationHtml = '';

        // Previous button
        if (state.currentPage > 1) {
            paginationHtml += `
                <div class="page-item" data-page="${state.currentPage - 1}">
                    <i class="fas fa-angle-left"></i>
                </div>
            `;
        }

        // Page numbers
        // Limit visible page numbers for better performance with large page counts
        const maxVisiblePages = 5;
        let startPage = Math.max(1, state.currentPage - Math.floor(maxVisiblePages / 2));
        let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

        // Adjust start page if we're near the end
        if (endPage - startPage + 1 < maxVisiblePages) {
            startPage = Math.max(1, endPage - maxVisiblePages + 1);
        }

        // Add first page and ellipsis if needed
        if (startPage > 1) {
            paginationHtml += `
                <div class="page-item ${1 === state.currentPage ? 'active' : ''}" data-page="1">1</div>
            `;

            if (startPage > 2) {
                paginationHtml += `<div class="page-item-ellipsis">...</div>`;
            }
        }

        // Add page numbers
        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `
                <div class="page-item ${i === state.currentPage ? 'active' : ''}" data-page="${i}">${i}</div>
            `;
        }

        // Add last page and ellipsis if needed
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                paginationHtml += `<div class="page-item-ellipsis">...</div>`;
            }

            paginationHtml += `
                <div class="page-item ${totalPages === state.currentPage ? 'active' : ''}" data-page="${totalPages}">${totalPages}</div>
            `;
        }

        // Next button
        if (state.currentPage < totalPages) {
            paginationHtml += `
                <div class="page-item" data-page="${state.currentPage + 1}">
                    <i class="fas fa-angle-right"></i>
                </div>
            `;
        }

        // Set HTML content once (more efficient than multiple appends)
        paginationContainer.html(paginationHtml);

        // Use event delegation for better performance
        paginationContainer.off('click', '.page-item').on('click', '.page-item', function() {
            state.currentPage = parseInt($(this).data('page'));
            filterHotels();
        });
    }

    /**
     * Load user reservations from API
     */
    function loadReservations() {
        if (!state.isLoggedIn) return;

        DOM.reservations.reservationsLoader().removeClass('hidden');
        DOM.reservations.reservationCards().addClass('hidden');

        // Fallback mock data in case the API fails
        const fallbackReservations = [
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

        $.ajax({
            url: `${config.apiEndpoints.bookings}/${state.currentUser.id}`,
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                DOM.reservations.reservationsLoader().addClass('hidden');
                state.reservations = data;
                renderReservations(state.reservations);
                console.log('Loaded reservations:', state.reservations);
                DOM.reservations.reservationCards().removeClass('hidden');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                DOM.reservations.reservationsLoader().addClass('hidden');
                console.error('Error loading reservations:', textStatus, errorThrown);
                // Fallback to mock data if API fails
                state.reservations = fallbackReservations;
                renderReservations(state.reservations);
                DOM.reservations.reservationCards().removeClass('hidden');
            }
        });
    }

    function renderReservations(reservations) {
        const reservationCardsContainer = DOM.reservations.reservationCards();
        reservationCardsContainer.empty();

        if (!reservations || reservations.length === 0) {
            reservationCardsContainer.html('<p class="no-reservations">You have no reservations yet.</p>');
            return;
        }

        // Build HTML string for better performance
        let reservationsHtml = '';

        reservations.forEach(reservation => {
            reservationsHtml += `
                <div class="reservation-card" data-reservation-id="${reservation.id}">
                    <h3 class="reservation-hotel">${reservation.hotelName}</h3>
                    <p class="reservation-details">${reservation.roomType}</p>
                    <p class="reservation-date">Check-in: ${formatDate(reservation.checkinDate)}</p>
                    <p class="reservation-price">$${reservation.price}</p>
                    ${reservation.cancelable ?
                `<button class="cancel-button" data-reservation-id="${reservation.id}">Cancel Reservation</button>` :
                ''}
                </div>
            `;
        });

        // Set HTML content once (more efficient than multiple appends)
        reservationCardsContainer.html(reservationsHtml);

        // Use event delegation for better performance
        reservationCardsContainer.off('click', '.cancel-button').on('click', '.cancel-button', function() {
            const reservationId = $(this).data('reservation-id');
            cancelReservation(reservationId);
        });
    }

    function filterHotels() {
        const selectedAmenities = $('input[name="amenity"]:checked').map(function () {
            return $(this).val();
        }).get();

        const searchTerm = DOM.hotels.searchInput().val().toLowerCase();

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
        DOM.hotels.searchBtn().on('click', function () {
            filterHotels();
        });

        // Enter key on search input
        DOM.hotels.searchInput().on('keyup', function (e) {
            if (e.key === 'Enter') {
                filterHotels();
            }
        });

        // Login button click
        DOM.auth.loginBtn().on('click', function () {
            showLoginModal();
        });

        DOM.auth.sidebarLoginBtn().on('click', function () {
            showLoginModal();
        });

        // Sign up button click
        DOM.auth.signupBtn().on('click', function () {
            showSignupModal();
        });

        // Logout button click
        DOM.auth.logoutBtn().on('click', function () {
            logout();
        });

        // Modal close buttons
        DOM.modals.login.closeBtn().on('click', function() {
            DOM.modals.login.modal().addClass('hidden');
        });

        DOM.modals.signup.closeBtn().on('click', function() {
            DOM.modals.signup.modal().addClass('hidden');
        });

        // Notification modal close button
        DOM.modals.notification.closeBtn().on('click', function() {
            DOM.modals.notification.modal().addClass('hidden');
        });

        // Notification modal OK button
        DOM.modals.notification.okBtn().on('click', function() {
            DOM.modals.notification.modal().addClass('hidden');
        });

        // Switch between login and signup modals
        DOM.modals.login.switchToSignup().on('click', function(e) {
            e.preventDefault();
            DOM.modals.login.modal().addClass('hidden');
            DOM.modals.signup.modal().removeClass('hidden');
        });

        DOM.modals.signup.switchToLogin().on('click', function(e) {
            e.preventDefault();
            DOM.modals.signup.modal().addClass('hidden');
            DOM.modals.login.modal().removeClass('hidden');
        });

        // Form submissions
        DOM.modals.login.form().on('submit', function(e) {
            e.preventDefault();
            const email = DOM.modals.login.emailInput().val();
            const password = DOM.modals.login.passwordInput().val();

            if (!email || !password) return;

            login(email, password);
            DOM.modals.login.modal().addClass('hidden');
        });

        DOM.modals.signup.form().on('submit', function(e) {
            e.preventDefault();
            const firstName = DOM.modals.signup.firstNameInput().val();
            const lastName = DOM.modals.signup.lastNameInput().val();
            const email = DOM.modals.signup.emailInput().val();
            const password = DOM.modals.signup.passwordInput().val();

            if (!firstName || !lastName || !email || !password) return;

            signup(firstName, lastName, email, password);
            DOM.modals.signup.modal().addClass('hidden');
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
        // Show loading or disable button
        const cancelButton = $(`.cancel-button[data-reservation-id="${reservationId}"]`)
            .text('Cancelling...')
            .prop('disabled', true);

        // In a real app, this would be an AJAX call to the backend
        // Here we'll use fetch for better performance
        fetch(`${config.apiEndpoints.bookings}/${reservationId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to cancel reservation');
            }

            // Remove the reservation from the list
            state.reservations = state.reservations.filter(res => res.id !== reservationId);

            // Re-render reservations
            renderReservations(state.reservations);

            // Show success message
            showSuccess('Reservation cancelled successfully!');
        })
        .catch(error => {
            console.error('Error cancelling reservation:', error);

            // Re-enable the button
            cancelButton.text('Cancel Reservation').prop('disabled', false);

            // Show error message
            showError('Failed to cancel reservation. Please try again.');
        });
    }

    function formatDate(dateString) {
        const options = {year: 'numeric', month: 'long', day: 'numeric'};
        return new Date(dateString).toLocaleDateString(undefined, options);
    }

    /**
     * Show error message
     * @param {string} message - Error message
     */
    function showError(message) {
        // Show error notification using the modal
        $('#notificationTitle').text('Error');
        $('#notificationMessage').text(message);
        $('#notificationModal').removeClass('hidden');
    }

    /**
     * Show success message
     * @param {string} message - Success message
     */
    function showSuccess(message) {
        // Show success notification using the modal
        $('#notificationTitle').text('Success');
        $('#notificationMessage').text(message);
        $('#notificationModal').removeClass('hidden');
    }

    // Login/Signup Modal functions
    function showLoginModal() {
        // Clear previous inputs
        DOM.modals.login.emailInput().val('');
        DOM.modals.login.passwordInput().val('');

        // Show the modal
        DOM.modals.login.modal().removeClass('hidden');
    }

    function showSignupModal() {
        // Clear previous inputs
        DOM.modals.signup.firstNameInput().val('');
        DOM.modals.signup.lastNameInput().val('');
        DOM.modals.signup.emailInput().val('');
        DOM.modals.signup.passwordInput().val('');

        // Show the modal
        DOM.modals.signup.modal().removeClass('hidden');
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
                showSuccess('Login successful!');
            },
            error: (xhr, status, error) => {
                if (xhr.status === 500) {
                    console.error("Server Error:", xhr.responseText);
                } else if (xhr.status === 401 || xhr.status === 400) {
                    showError('Wrong credentials. Please try again');
                    console.error("Unhandled Error:", xhr.status, xhr.responseText);
                }

                showError('Failed to submit review. Please try again.');
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
                showSuccess('SignUp & Login successful!');
            },
            error: (xhr, status, error) => {
                if (xhr.status === 500) {
                    console.error("Server Error:", xhr.responseText);
                } else if (xhr.status === 401 || xhr.status === 400) {
                    console.error("Unhandled Error:", xhr.status, xhr.responseText);
                }

                showError('Error creating your account. Please try again');
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

        showSuccess('You have been logged out successfully.');
    }

    // Initialize the page
    init();

    // Expose loadReservations function to global scope for mobile button
    window.loadReservations = loadReservations;
})();
