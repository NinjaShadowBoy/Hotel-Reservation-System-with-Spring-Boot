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
(function() {
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
            payment: $('#paymentForm')
        },
        modals: {
            payment: $('#paymentModal'),
            closePayment: $('#closePaymentModal')
        },
        buttons: {
            leaveReview: $('#leaveReview'),
            writeFAQ: $('#writeFAQ')
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
        } else {
            state.isLoggedIn = true; // Default to true for now (should be properly implemented)
        }
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
    }

    /**
     * Set up rating stars interaction
     */
    function setupRatingStars() {
        if (!state.isLoggedIn) return;

        const $ratingStars = $('#reviewForm .rating-stars');

        // Hover effect
        $ratingStars.on('mouseover', 'svg', function() {
            const rating = $ratingStars.find('svg').index(this) + 1;
            highlightStars(rating);
        });

        // Click to select
        $ratingStars.on('click', 'svg', function() {
            const $star = $(this);
            state.currentRating = $star.index() + 1;
            setRating(state.currentRating);
            updateRatingInput(state.currentRating);
        });

        // Reset on mouse leave
        $ratingStars.on('mouseleave', function() {
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
        $('#reviewForm .rating-stars svg').removeClass('active preview');
        $('#reviewForm .rating-stars svg').slice(0, rating).addClass('preview');
    }

    /**
     * Set active rating
     * @param {number} rating - Rating value (1-5)
     */
    function setRating(rating) {
        $('#reviewForm .rating-stars svg').removeClass('active preview');
        $('#reviewForm .rating-stars svg').slice(0, rating).addClass('active');
    }

    /**
     * Clear star highlighting
     */
    function clearHighlight() {
        $('#reviewForm .rating-stars svg').removeClass('active preview');
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
        // Check login status (commented out for now)
        // if (!state.isLoggedIn) {
        //     showLoginModal();
        //     return;
        // }

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

        if (!state.stripe || !state.cardElement) {
            showError('Payment processing is not available');
            return;
        }

        try {
            const {error, paymentMethod} = await state.stripe.createPaymentMethod({
                type: 'card',
                card: state.cardElement
            });

            if (error) {
                showError(error.message);
                return;
            }

            // Process reservation
            $.ajax({
                url: `${config.apiEndpoints.reservation}/${state.currentUser.id}`,
                type: 'POST',
                data: {
                    roomTypeId: state.selectedRoomType,
                    clientId: state.currentUser.id
                },
                success: () => {
                    showSuccess('Booking successful!');
                    DOM.modals.payment.addClass('hidden');
                },
                error: (xhr) => {
                    showError('Failed to complete booking. Please try again.');
                    console.error('Booking error:', xhr.responseText);
                }
            });
        } catch (err) {
            showError('Payment processing failed');
            console.error('Payment error:', err);
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
            data: {
                text: reviewText,
                rating: state.currentRating
            },
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
            data: {
                question: question
            },
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

    // Initialize when document is ready
    $(document).ready(init);
})();
