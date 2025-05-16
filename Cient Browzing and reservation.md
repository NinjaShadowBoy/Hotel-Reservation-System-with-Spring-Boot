Covers use cases:
- 

## 1. Hotel browzing
The page where the client lands when he enters the website
url: `/client/home`

DTOs
```java
class HotelDetails {
    Number id;

    String name;
    String image;
    String location;
    String desc;
    Number rating;

    List<String> services;
}

class ClientReservation {
    Number id;

    String hotelName;
    String roomType;
    Number price;
    Date date;
    Date checkinDate;

    Bool cancelable;
}
```

Directly on page load the front:
- GET `List<String>` at `/api/services`: Theses are the services that may be offered by a hotel; Used to filter hotels;
- GET `List<HotelDetails>` at `/api/hotels`: These are the hotels to be displayed for the user to see and they will have a link to their own page for reservation
- GET `List<ClientReservation>`: This i the list of all reservations made by the client. If the reservation canbe cancelled there will be a button or something to cancel it.


## 1. Hotel reservation
On the hotel browzing page, if the client clicks on a hotel he/she will land here
url: `/client/reservation/{hotelId}`

DTOs 
```java
class RoomType {
    Number id;

    String label;
    List<String> services;
    Number price;
    String image;
}


class Reservation {
    Number roomTypeId;
    Number clientId;
}

class Review {
    String author;
    Date date;
    String text;
    Number rating;
}

class FAQ {
    String question;
    String answer;
}
```

Directly on page load the front:
- GET `List<RoomType>` at `/api/roomtype/{hotelId}`
- GET `List<Review>` at `/api/reviews/{hotelId}`
- GET `List<FAQ>` at `/api/faq/{hotelId}`

### Hotel reservation
Client must login first.
POST `List<Reservation>` at `/api/reservation/{clientId}` and get back
`String` as response.

### Reviewing a hotel
Client must login first.
POST `Review` at `/api/reviews/{hotelId}/{clientId}` and get back
`String` as response.

### Asking a FAQ
Client must login first.
POST `FAQ` at `/api/faq/{hotelId}/{clientId}`  and get back
`String` as response.