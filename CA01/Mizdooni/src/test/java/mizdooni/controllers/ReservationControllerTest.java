package mizdooni.controllers;

import jdk.jfr.Label;
import mizdooni.exceptions.*;
import mizdooni.model.Address;
import mizdooni.model.Reservation;
import mizdooni.model.Restaurant;
import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.ReservationService;
import mizdooni.service.RestaurantService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static mizdooni.controllers.ControllerUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    private RestaurantService restaurantService = Mockito.mock(RestaurantService.class);
    private ReservationService reserveService = Mockito.mock(ReservationService.class);
    @InjectMocks
    private ReservationController reservationController;

    @Label("getReservations")
    @Test
    public void given_invalid_restaurant_id_when_getting_reservations_then_responses_not_found(){
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getReservations(9999, 1,""));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }

    @Label("getReservations")
    @Test
    public void given_invalid_date_when_getting_reservations_then_throws_bad_request_param_bad_type(){
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getReservations(restaurant.getId(), 1,""));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals(PARAMS_BAD_TYPE,exception.getMessage());
    }

    @Label("getReservations")
    @Test
    public void given_non_available_restaurant_when_getting_reservations_then_throws_bad_request() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        Mockito.when(reserveService.getReservations(restaurant.getId(),1, LocalDate.parse("2020-12-12", DATE_FORMATTER))).thenThrow(new RuntimeException("error"));

        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getReservations(restaurant.getId(), 1,"2020-12-12"));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }

    @Label("getReservations")
    @Test
    public void given_valid_input_when_getting_reservations_then_responses_ok(){
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        List<Reservation> reservationList = new ArrayList<>();
        try {
            Mockito.when(reserveService.getReservations(restaurant.getId(),1, LocalDate.parse("2020-12-12", DATE_FORMATTER))).thenReturn(reservationList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(Response.ok("restaurant table reservations", reservationList),reservationController.getReservations(restaurant.getId(), 1,"2020-12-12"));
    }


    @Label("getCustomerReservations")
    @Test
    public void given_valid_input_when_getting_customer_reservations_then_responses_ok() throws UserNotFound, UserNoAccess {
        int customer_id = 1;
        List<Reservation> reservationList = new ArrayList<>();
        Mockito.when(reserveService.getCustomerReservations(customer_id)).thenReturn(reservationList);
        assertEquals(Response.ok("user reservations", reservationList),reservationController.getCustomerReservations(customer_id));
    }

    @Label("getCustomerReservations")
    @Test
    public void given_invalid_input_when_getting_customer_reservations_then_throws_bad_request() throws UserNotFound, UserNoAccess {
        int customer_id = 1;
        List<Reservation> reservationList = new ArrayList<>();
        Mockito.when(reserveService.getCustomerReservations(customer_id)).thenThrow(new RuntimeException("error"));
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getCustomerReservations(customer_id));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }


    @Label("getAvailableTimes")
    @Test
    public void given_invalid_restaurant_id_when_getting_available_times_then_responses_not_found(){
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getAvailableTimes(9999, 1,""));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }

    @Label("getAvailableTimes")
    @Test
    public void given_invalid_date_when_getting_available_times_then_throws_bad_request_param_bad_type(){
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getAvailableTimes(restaurant.getId(), 1,""));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals(PARAMS_BAD_TYPE,exception.getMessage());
    }

    @Label("getAvailableTimes")
    @Test
    public void given_non_available_restaurant_when_getting_available_times_then_throws_bad_request() throws RestaurantNotFound, DateTimeInThePast, BadPeopleNumber {
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        Mockito.when(reserveService.getAvailableTimes(restaurant.getId(),1, LocalDate.parse("2020-12-12", DATE_FORMATTER))).thenThrow(new RuntimeException("error"));

        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.getAvailableTimes(restaurant.getId(), 1,"2020-12-12"));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }

    @Label("getAvailableTimes")
    @Test
    public void given_valid_input_when_getting_available_times_then_responses_ok() throws DateTimeInThePast, RestaurantNotFound, BadPeopleNumber {
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        List<LocalTime> availableTimes = new ArrayList<>();
        Mockito.when(reserveService.getAvailableTimes(restaurant.getId(),1, LocalDate.parse("2020-12-12", DATE_FORMATTER))).thenReturn(availableTimes);

        assertEquals(Response.ok("available times", availableTimes),reservationController.getAvailableTimes(restaurant.getId(), 1,"2020-12-12"));
    }


    @Label("addReservation")
    @Test
    public void given_invalid_restaurant_id_when_adding_reservation_then_responses_not_found(){
        Map<String,String> params = Map.of();
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.addReservation(9999, params));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }

    @Label("addReservation")
    @Test
    public void given_invalid_params_when_adding_reservation_then_throws_bad_request_param_missing(){
        String people = "people";
        Map<String,String> params = Map.of("people",people);
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.addReservation(restaurant.getId(), params));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals(PARAMS_MISSING,exception.getMessage());
    }

    @Label("addReservation")
    @Test
    public void given_invalid_date_when_adding_reservation_then_throws_bad_request_param_bad_type(){
        String people = "people",datetime = "datetime";
        Map<String,String> params = Map.of("people",people,"datetime",datetime);
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.addReservation(restaurant.getId(), params));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals(PARAMS_BAD_TYPE,exception.getMessage());
    }

    @Label("addReservation")
    @Test
    public void given_non_available_restaurant_when_adding_reservation_then_throws_bad_request() throws RestaurantNotFound, DateTimeInThePast, UserNotFound, TableNotFound, ReservationNotInOpenTimes, ManagerReservationNotAllowed, InvalidWorkingTime {
        String people = "people",datetime = "2020-12-12 12:20";
        Map<String,String> params = Map.of("people",people,"datetime",datetime);
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        Mockito.when(reserveService.reserveTable(restaurant.getId(),1, LocalDateTime.parse(datetime, DATETIME_FORMATTER))).thenThrow(new RuntimeException("error"));

        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.addReservation(restaurant.getId(), params));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }

    @Label("addReservation")
    @Test
    public void given_valid_input_when_adding_reservation_then_responses_ok() throws DateTimeInThePast, RestaurantNotFound, UserNotFound, TableNotFound, ReservationNotInOpenTimes, ManagerReservationNotAllowed, InvalidWorkingTime {
        String people = "1",datetime = "2020-12-12 12:20";
        Map<String,String> params = Map.of("people",people,"datetime",datetime);
        Restaurant restaurant = new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link");
        Mockito.when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        Reservation reservation = Mockito.mock(Reservation.class);
        Mockito.when(reserveService.reserveTable(restaurant.getId(),1, LocalDateTime.parse(datetime, DATETIME_FORMATTER))).thenReturn(reservation);

        assertEquals(Response.ok("reservation done", reservation),reservationController.addReservation(restaurant.getId(), params));
    }






    @Label("cancelReservation")
    @Test
    public void given_valid_input_when_cancel_reservations_then_responses_ok() throws UserNotFound, UserNoAccess {
        int reservationNumber = 1;
        assertEquals(Response.ok("reservation cancelled"),reservationController.cancelReservation(reservationNumber));
    }

    @Label("cancelReservation")
    @Test
    public void given_invalid_input_when_cancel_reservations_then_throws_bad_request() throws UserNotFound, ReservationCannotBeCancelled, ReservationNotFound {
        int reservationNumber = 1;
        doThrow(new RuntimeException("error")).when(reserveService).cancelReservation(reservationNumber);
        ResponseException exception =  assertThrows(ResponseException.class,() -> reservationController.cancelReservation(reservationNumber));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }
}
