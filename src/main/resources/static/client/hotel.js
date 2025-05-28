/**
 * Hotel Details Page JavaScript
 *
 * This file handles the functionality of the hotel details page, including:
 * - Loading and displaying hotel information
 * - Displaying room types, reviews, and FAQs
 * - Handling user interactions (booking, reviews, FAQs)
 * - Processing payments via Stripe
 */

// Use IIFE to avoid polluting global namespace
(function () {
    'use strict';

    // App configuration
    const config = {
        stripeKey: 'pk_test_51RQpf6QW2O1tpdCCPDtewogSRFwA5B8AJcFmY7zgZ9tlCEU2WEnW3nAbxt5gwb7icdY116Tf4WiwmDi31tpvVoJ700CnAVXSct',
        apiEndpoints: {
            hotels: '/api/hotels',
            roomTypes: '/api/roomtype',
            reviews: '/api/reviews',
            faq: '/api/faq',
            reservation: '/api/reservation'
        }
    };

    // State management
    const state = {
        hotelId: null,
        currentUser: null,
        isLoggedIn: false,
        selectedRoomType: null,
        currentRating: 0,
        stripe: null,
        elements: null,
        cardElement: null
    };

    // DOM elements cache
    const DOM = {
        hotelInfo: {
            name: $('#hotelName'),
            rating: $('#hotelRating'),
            image: $('#hotelImage'),
            description: $('#hotelDescription'),
            location: $('#hotelLocation')
        },
        roomGrid: $('#roomGrid'),
        reviewsList: $('#reviewsList'),
        faqList: $('#faqList'),
        forms: {
            review: $('#reviewForm'),
            faq: $('#faqForm'),
            payment: $('#paymentForm'),
            login: $('#loginForm'),
            signup: $('#signupForm')
        },
        modals: {
            payment: $('#paymentModal'),
            closePayment: $('#closePaymentModal'),
            login: $('#loginModal'),
            closeLogin: $('#closeLoginModal'),
            signup: $('#signupModal'),
            closeSignup: $('#closeSignupModal')
        },
        buttons: {
            leaveReview: $('#leaveReview'),
            writeFAQ: $('#writeFAQ'),
            login: $('#loginBtn'),
            signup: $('#signupBtn'),
            logout: $('#logoutBtn'),
            switchToSignup: $('#switchToSignup'),
            switchToLogin: $('#switchToLogin')
        },
        auth: {
            authButtons: $('#authButtons'),
            userProfile: $('#userProfile'),
            welcomeUser: $('#welcomeUser')
        }
    };

    /**
     * Initialize the page
     */
    function init() {
        // Extract hotel ID from URL
        state.hotelId = window.location.pathname.split('/').pop();

        // Check login status
        checkLoginStatus();

        // Initialize Stripe if available
        initializeStripe();

        // Load all data
        loadHotelData();

        // Set up event listeners
        setupEventListeners();
    }

    /**
     * Check if user is logged in
     */
    function checkLoginStatus() {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            state.currentUser = JSON.parse(savedUser);
            state.isLoggedIn = true;
            updateUIForLoggedInUser();
        } else {
            state.isLoggedIn = false;
            updateUIForLoggedOutUser();
        }
    }

    /**
     * Update UI for logged in user
     */
    function updateUIForLoggedInUser() {
        DOM.auth.authButtons.addClass('hidden');
        DOM.auth.userProfile.removeClass('hidden');
        DOM.auth.welcomeUser.text(`Welcome, ${state.currentUser.firstName}`);
        DOM.forms.review.removeClass('hidden');
        DOM.forms.faq.removeClass('hidden');
        DOM.buttons.leaveReview.removeClass('hidden');
        DOM.buttons.writeFAQ.removeClass('hidden');
    }

    /**
     * Update UI for logged out user
     */
    function updateUIForLoggedOutUser() {
        DOM.auth.authButtons.removeClass('hidden');
        DOM.auth.userProfile.addClass('hidden');
    }

    /**
     * Initialize Stripe payment processing
     */
    function initializeStripe() {
        try {
            state.stripe = Stripe(config.stripeKey);
            state.elements = state.stripe.elements();
            state.cardElement = state.elements.create('card');
        } catch (e) {
            console.error('Failed to initialize Stripe:', e);
            state.stripe = null;
            state.elements = null;
            state.cardElement = null;
        }
    }

    /**
     * Set up all event listeners
     */
    function setupEventListeners() {
        // Rating stars interaction
        setupRatingStars();

        // Booking button click
        $(document).on('click', '.book-btn', handleBookingClick);

        // Payment form submission
        DOM.forms.payment.submit(handlePaymentSubmission);

        // Payment modal close
        DOM.modals.closePayment.on('click', () => {
            DOM.modals.payment.addClass('hidden');
        });

        // Review form submission
        DOM.forms.review.submit(handleReviewSubmission);

        // FAQ form submission
        DOM.forms.faq.submit(handleFAQSubmission);

        // Login button click
        DOM.buttons.login.on('click', function () {
            showLoginModal();
        });

        // Signup button click
        DOM.buttons.signup.on('click', function () {
            showSignupModal();
        });

        // Logout button click
        DOM.buttons.logout.on('click', function () {
            logout();
        });

        // Login modal close
        DOM.modals.closeLogin.on('click', function () {
            DOM.modals.login.addClass('hidden');
        });

        // Signup modal close
        DOM.modals.closeSignup.on('click', function () {
            DOM.modals.signup.addClass('hidden');
        });

        // Switch between login and signup modals
        DOM.buttons.switchToSignup.on('click', function (e) {
            e.preventDefault();
            DOM.modals.login.addClass('hidden');
            DOM.modals.signup.removeClass('hidden');
        });

        DOM.buttons.switchToLogin.on('click', function (e) {
            e.preventDefault();
            DOM.modals.signup.addClass('hidden');
            DOM.modals.login.removeClass('hidden');
        });

        // Login form submission
        DOM.forms.login.on('submit', function (e) {
            e.preventDefault();
            const email = $('#loginEmail').val();
            const password = $('#loginPassword').val();

            login(email, password);
            DOM.modals.login.addClass('hidden');
        });

        // Signup form submission
        DOM.forms.signup.on('submit', function (e) {
            e.preventDefault();
            const firstName = $('#signupFirstName').val();
            const lastName = $('#signupLastName').val();
            const email = $('#signupEmail').val();
            const password = $('#signupPassword').val();

            signup(firstName, lastName, email, password);
            DOM.modals.signup.addClass('hidden');
        });
    }

    /**
     * Set up rating stars interaction
     */
    function setupRatingStars() {
        if (!state.isLoggedIn) return;

        const $ratingStars = $('#reviewForm .rating-stars');

        // Hover effect
        $ratingStars.on('mouseover', 'i', function () {
            const rating = $ratingStars.find('i').index(this) + 1;
            highlightStars(rating);
        });

        // Click to select
        $ratingStars.on('click', 'i', function () {
            const $star = $(this);
            state.currentRating = $star.index() + 1;
            setRating(state.currentRating);
            updateRatingInput(state.currentRating);
        });

        // Reset on mouse leave
        $ratingStars.on('mouseleave', function () {
            if (state.currentRating > 0) {
                setRating(state.currentRating);
            } else {
                clearHighlight();
            }
        });
    }

    /**
     * Highlight stars for preview
     * @param {number} rating - Rating value (1-5)
     */
    function highlightStars(rating) {
        $('#reviewForm .rating-stars i').removeClass('active preview');
        $('#reviewForm .rating-stars i').slice(0, rating).addClass('preview');
    }

    /**
     * Set active rating
     * @param {number} rating - Rating value (1-5)
     */
    function setRating(rating) {
        $('#reviewForm .rating-stars i').removeClass('active preview');
        $('#reviewForm .rating-stars i').slice(0, rating).addClass('active');
    }

    /**
     * Clear star highlighting
     */
    function clearHighlight() {
        $('#reviewForm .rating-stars i').removeClass('active preview');
    }

    /**
     * Update hidden rating input
     * @param {number} rating - Rating value (1-5)
     */
    function updateRatingInput(rating) {
        const $form = DOM.forms.review;
        let $ratingInput = $form.find('input[name="rating"]');

        // Create hidden input if it doesn't exist
        if ($ratingInput.length === 0) {
            $ratingInput = $('<input>', {
                type: 'hidden',
                name: 'rating'
            });
            $form.append($ratingInput);
        }

        $ratingInput.val(rating);
    }

    /**
     * Handle booking button click
     */
    function handleBookingClick() {
        // Check login status
        if (!state.isLoggedIn) {
            showLoginModal();
            return;
        }

        const roomId = $(this).data('room-id');
        state.selectedRoomType = roomId;

        // Find the room data
        const $roomCard = $(this).closest('.room-card');
        const roomType = $roomCard.find('h3').text();
        const roomPrice = $roomCard.find('.room-price').text().trim();
        const roomServices = $roomCard.find('.room-services').html();

        // Populate room info in the payment modal
        $('#roomInfoType').text(roomType);
        $('#roomInfoPrice').text(roomPrice);
        $('#roomInfoServices').html(roomServices);

        // Set min and max dates for check-in datetime
        const today = new Date();
        const oneMonthLater = new Date();
        oneMonthLater.setMonth(today.getMonth() + 1);

        // Format dates for datetime-local input
        const todayStr = today.toISOString().slice(0, 16);
        const oneMonthLaterStr = oneMonthLater.toISOString().slice(0, 16);

        $('#checkinDatetime').attr('min', todayStr);
        $('#checkinDatetime').attr('max', oneMonthLaterStr);
        $('#checkinDatetime').val(todayStr);

        // Show the modal
        DOM.modals.payment.removeClass('hidden');

        if (state.cardElement) {
            state.cardElement.mount('#cardElement');
        }
    }

    /**
     * Handle payment form submission
     * @param {Event} e - Form submit event
     */
    async function handlePaymentSubmission(e) {
        e.preventDefault();

        // Disable submit button to prevent double submissions
        $('#submit').prop('disabled', true);

        if (!state.stripe || !state.cardElement) {
            showError('Payment processing is not available');
            $('#submit').prop('disabled', false);
            return;
        }

        // Validate check-in datetime
        const checkinDatetime = $('#checkinDatetime').val();
        if (!checkinDatetime) {
            showError('Please select a check-in date and time');
            $('#submit').prop('disabled', false);
            return;
        }

        const checkinDate = new Date(checkinDatetime);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Reset time to start of day for accurate comparison

        const oneMonthLater = new Date();
        oneMonthLater.setMonth(today.getMonth() + 1);

        if (checkinDate < today) {
            showError('Check-in date cannot be in the past');
            $('#submit').prop('disabled', false);
            return;
        }

        if (checkinDate > oneMonthLater) {
            showError('Check-in date cannot be more than 1 month ahead');
            $('#submit').prop('disabled', false);
            return;
        }

        try {
            // Create payment method
            const {error, paymentMethod} = await state.stripe.createPaymentMethod({
                type: 'card',
                card: state.cardElement
            });

            if (error) {
                showError(error.message);
                $('#submit').prop('disabled', false);
                return;
            }

            // Create PaymentIntent on backend
            const response = await $.ajax({
                url: '/create-payment-intent',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    roomTypeId: state.selectedRoomType,
                    clientId: state.currentUser.id,
                    checkinDatetime: checkinDatetime,
                })
            });

            const clientSecret = response.clientSecret;
            console.log(`Client secret: ${clientSecret}`);

            // Confirm the payment
            const paymentResult = await state.stripe.confirmCardPayment(clientSecret, {
                payment_method: {
                    card: state.cardElement,
                    billing_details: {
                        name: 'Customer Name' // Consider getting this from a form field
                    }
                }
            });

            if (paymentResult.error) {
                showError(paymentResult.error.message);
                $('#submit').prop('disabled', false);
                return;
            }

            if (paymentResult.paymentIntent.status === 'succeeded') {
                // Payment successful, now create the reservation
                try {
                    // await $.ajax({
                    //     url: `${config.apiEndpoints.reservation}/${state.currentUser.id}`,
                    //     type: 'POST',
                    //     contentType: 'application/json',
                    //     data: JSON.stringify({
                    //         roomTypeId: state.selectedRoomType,
                    //         clientId: state.currentUser.id,
                    //         checkinDatetime: checkinDatetime,
                    //         paymentIntentId: paymentResult.paymentIntent.id
                    //     })
                    // });

                    showSuccess('Booking successful!');
                    DOM.modals.payment.addClass('hidden');

                    // Optional: Reset form or redirect
                    // resetBookingForm();

                } catch (reservationError) {
                    // Payment succeeded but reservation failed
                    // You may want to implement a refund mechanism here
                    showError('Payment processed but booking failed. Please contact support.');
                    console.error('Reservation error:', reservationError.responseText || reservationError);
                }
            }

        } catch (err) {
            showError('Payment processing failed. Please try again.');
            console.error('Payment error:', err);
            $('#submit').prop('disabled', false);
        }

    }

    /**
     * Handle review form submission
     * @param {Event} e - Form submit event
     */
    function handleReviewSubmission(e) {
        e.preventDefault();

        const reviewText = DOM.forms.review.find('textarea').val();

        if (!reviewText || state.currentRating === 0) {
            showError('Please provide both a review text and rating');
            return;
        }

        $.ajax({
            url: `${config.apiEndpoints.reviews}/${state.hotelId}/${state.currentUser.id}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                text: reviewText,
                rating: state.currentRating
            }),
            success: () => {
                showSuccess('Review submitted!');
                DOM.forms.review.find('textarea').val('');
                state.currentRating = 0;
                clearHighlight();
                loadReviews();
            },
            error: (xhr) => {
                showError('Failed to submit review. Please try again.');
                console.error('Review submission error:', xhr.responseText);
            }
        });
    }

    /**
     * Handle FAQ form submission
     * @param {Event} e - Form submit event
     */
    function handleFAQSubmission(e) {
        e.preventDefault();

        const question = DOM.forms.faq.find('input').val();

        if (!question) {
            showError('Please enter your question');
            return;
        }

        $.ajax({
            url: `${config.apiEndpoints.faq}/${state.hotelId}/${state.currentUser.id}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                question: question
            }),
            success: () => {
                showSuccess('Question submitted!');
                DOM.forms.faq.find('input').val('');
                loadFAQs();
            },
            error: (xhr) => {
                showError('Failed to submit question. Please try again.');
                console.error('FAQ submission error:', xhr.responseText);
            }
        });
    }

    /**
     * Load all hotel data
     */
    function loadHotelData() {
        loadHotelDetails();
        loadRoomTypes();
        loadReviews();
        loadFAQs();
    }

    /**
     * Load hotel details
     */
    function loadHotelDetails() {
        $.ajax({
            url: `${config.apiEndpoints.hotels}/${state.hotelId}`,
            type: 'GET',
            success: (hotel) => {
                updateHotelUI(hotel);
            },
            error: (xhr) => {
                showError('Failed to load hotel details');
                console.error('Hotel details error:', xhr.responseText);
            }
        });
    }

    /**
     * Update UI with hotel details
     * @param {Object} hotel - Hotel data
     */
    function updateHotelUI(hotel) {
        DOM.hotelInfo.name.text(hotel.name);
        DOM.hotelInfo.rating.text(hotel.rating.toFixed(1));
        DOM.hotelInfo.image.attr('src', hotel.image);
        DOM.hotelInfo.description.text(hotel.desc);
        DOM.hotelInfo.location.text(hotel.location);

        // Show forms if logged in
        if (state.isLoggedIn) {
            DOM.forms.review.removeClass('hidden');
            DOM.forms.faq.removeClass('hidden');
            DOM.buttons.leaveReview.removeClass('hidden');
            DOM.buttons.writeFAQ.removeClass('hidden');
        }
    }

    /**
     * Load room types
     */
    function loadRoomTypes() {
        $.ajax({
            url: `${config.apiEndpoints.roomTypes}/${state.hotelId}`,
            type: 'GET',
            success: (rooms) => {
                renderRoomTypes(rooms);
            },
            error: (xhr) => {
                showError('Failed to load room types');
                console.error('Room types error:', xhr.responseText);
            }
        });
    }

    /**
     * Render room types in the UI
     * @param {Array} rooms - Room types data
     */
    function renderRoomTypes(rooms) {
        DOM.roomGrid.empty();

        rooms.forEach(room => {
            DOM.roomGrid.append(createRoomCard(room));
        });
    }

    /**
     * Create HTML for a room card
     * @param {Object} room - Room data
     * @returns {string} HTML for room card
     */
    function createRoomCard(room) {
        return `
            <div class="room-card">
                <img src="${room.image}" alt="${room.label}">
                <h3>${room.label}</h3>
                <div class="room-services">
                    ${renderRoomServices(room.services)}
                </div>
                <div class="room-price">$${room.price}
                    <span class="price-period">/night</span>
                </div>
                <button class="btn btn-primary book-btn" data-room-id="${room.id}">Book Now</button>
            </div>
        `;
    }

    /**
     * Render room services
     * @param {Array} services - Room services
     * @returns {string} HTML for services
     */
    function renderRoomServices(services) {
        return services.map(service => {
            const parts = service.toString().split(':');
            const serviceName = parts[0];
            const iconClass = parts[1];

            return `<span class="service-tag"><i class="${iconClass}"></i> ${serviceName}</span>`;
        }).join('');
    }

    /**
     * Load reviews
     */
    function loadReviews() {
        $.ajax({
            url: `${config.apiEndpoints.reviews}/${state.hotelId}`,
            type: 'GET',
            success: (reviews) => {
                renderReviews(reviews);
            },
            error: (xhr) => {
                showError('Failed to load reviews');
                console.error('Reviews error:', xhr.responseText);
            }
        });
    }

    /**
     * Render reviews in the UI
     * @param {Array} reviews - Reviews data
     */
    function renderReviews(reviews) {
        // Clear existing reviews but keep the "Leave a review" button
        const leaveReviewBtn = DOM.buttons.leaveReview.detach();
        DOM.reviewsList.empty().append(leaveReviewBtn);

        reviews.forEach(review => {
            DOM.reviewsList.append(createReviewCard(review));
        });
    }

    /**
     * Create HTML for a review card
     * @param {Object} review - Review data
     * @returns {string} HTML for review card
     */
    function createReviewCard(review) {
        return `
            <div class="review-card">
                <div class="review-header">
                    <span class="review-author">${review.author}</span>
                    <hr>
                    <div class="review-rating">
                        ${Array(Math.floor(review.rating)).fill('<i class="fas fa-star"></i>').join('')}
                    </div>
                </div>
                <p class="review-text">${review.text}</p>
                <span class="review-date">${new Date(review.date).toLocaleDateString()}</span>
            </div>
        `;
    }

    /**
     * Load FAQs
     */
    function loadFAQs() {
        $.ajax({
            url: `${config.apiEndpoints.faq}/${state.hotelId}`,
            type: 'GET',
            success: (faqs) => {
                renderFAQs(faqs);
            },
            error: (xhr) => {
                showError('Failed to load FAQs');
                console.error('FAQs error:', xhr.responseText);
            }
        });
    }

    /**
     * Render FAQs in the UI
     * @param {Array} faqs - FAQs data
     */
    function renderFAQs(faqs) {
        // Clear existing FAQs but keep the "Write FAQ" button
        const writeFAQBtn = DOM.buttons.writeFAQ.detach();
        DOM.faqList.empty().append(writeFAQBtn);

        faqs.forEach(faq => {
            DOM.faqList.append(createFAQCard(faq));
        });
    }

    /**
     * Create HTML for a FAQ card
     * @param {Object} faq - FAQ data
     * @returns {string} HTML for FAQ card
     */
    function createFAQCard(faq) {
        return `
            <div class="faq-card">
                <div class="faq-question">${faq.question}</div>
                <hr>
                <div class="faq-answer">${faq.answer}</div>
            </div>
        `;
    }

    /**
     * Show error message
     * @param {string} message - Error message
     */
    function showError(message) {
        alert(message); // Replace with a better UI notification in production
    }

    /**
     * Show success message
     * @param {string} message - Success message
     */
    function showSuccess(message) {
        alert(message); // Replace with a better UI notification in production
    }

    /**
     * Show login modal
     */
    function showLoginModal() {
        $('#loginEmail').val('');
        $('#loginPassword').val('');

        DOM.modals.login.removeClass('hidden');
    }

    /**
     * Show signup modal
     */
    function showSignupModal() {
        $('#signupFirstName').val('');
        $('#signupLastName').val('');
        $('#signupEmail').val('');
        $('#signupPassword').val('');

        DOM.modals.signup.removeClass('hidden');
    }

    /**
     * Login user
     * @param {string} email - User email
     * @param {string} password - User password
     */
    function login(email, password) {
        // Make an API call to authenticate the user

        $.ajax({
            url: `/api/login`,
            type: 'POST',
            contentType: 'application/json',
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
     * Signup user
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
     * Logout user
     */
    function logout() {
        localStorage.removeItem('currentUser');
        state.currentUser = null;
        state.isLoggedIn = false;

        updateUIForLoggedOutUser();
        showSuccess('Logout successful!');
    }

    // Initialize when document is ready
    $(document).ready(init);
})();
