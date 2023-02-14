package ru.practicum.shareit.gateway.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.booking.dto.BookingShort;
import ru.practicum.shareit.gateway.booking.dto.State;
import ru.practicum.shareit.gateway.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingShort bookingShort) {
        return post("", userId, null, bookingShort);
    }

    public ResponseEntity<Object> updateBooking(long bookingId, Long userId, String approved) {

        Map<String, Object> parameters = Map.of(
                "approved", approved
        );

        return patch("/"+bookingId+"?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(long bookingId, Long userId) {
        return get("/" + bookingId, userId, null);
    }

        public ResponseEntity<Object> getBookingsOfUser(long userId, State state, Integer from, Integer size) {
            Map<String, Object> parameters = Map.of(
                    "state", state.name(),
                    "from", from,
                    "size", size
            );
            return get("?state={state}&from={from}&size={size}", userId, parameters);

    }

    public ResponseEntity<Object> getBookingsOfUsersItems(Long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size

        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}
