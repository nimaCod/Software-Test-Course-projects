package mizdooni.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    List<Reservation> reservations;
    @Mock Address address;
    @Mock User.Role role;
    private User user;

    User createDummyUser(){
        return new User("nima","password","nima@google.com",address,role);
    }
    @BeforeEach
    void setup(){
        user = createDummyUser();
        reservations = Arrays.asList(
                Mockito.mock(Reservation.class),
                Mockito.mock(Reservation.class)
        );
    }

    @Tag("Test to check if the AddReservation works.")
    @Test
    public void testAddReservation(){
        Reservation reservation = reservations.get(0);
        user.addReservation(reservation);
        assertTrue(user.getReservations().contains(reservation));
        int reservationNumber = reservation.getReservationNumber();
        assertEquals(0,reservationNumber);
    }

    @Tag("Test if getReservation finds a reservation")
    @Test
    public void testGetReservationFindsReservation(){
        Reservation reservation = reservations.get(1);
        user.addReservation(reservation);
        int reservationNumber = reservation.getReservationNumber();
        assertEquals(reservation,user.getReservation(reservationNumber));
    }

    @Tag("Test if getReservation does not find a reservation because does not exist")
    @Test
    public void testGetReservationDoesNotFindReservation(){
        assertNull(user.getReservation(0));
    }

    @Tag("Test if getReservation does not find a reservation because it is cancelled")
    @Test
    public void testGetReservationDoesNotFindCancelledReservation(){
        Reservation reservation = reservations.get(0);
        user.addReservation(reservation);
        int reservationNumber = reservation.getReservationNumber();
        Mockito.when(reservations.get(0).isCancelled()).thenReturn(Boolean.TRUE);
        assertNull(user.getReservation(reservationNumber));
    }


    @Tag("Test if checkReservation returns True when there is a reservation")
    @Test
    public void testCheckReservationReturnsTrue(){
        Restaurant restaurant = Mockito.mock(Restaurant.class);
        Reservation reservation = Mockito.mock(Reservation.class);

        Mockito.when(reservation.getRestaurant()).thenReturn(restaurant);
        Mockito.when(reservation.isCancelled()).thenReturn(false);
        Mockito.when(reservation.getDateTime()).thenReturn(LocalDateTime.now().minusDays(1));

        user.addReservation(reservation);
        assertTrue(user.checkReserved(restaurant));
    }

    @Tag("Test if checkReservation returns False when there isn't a reservation")
    @Test
    public void testCheckReservationReturnsFalseWhenThereIsNoReservation(){
        Restaurant restaurant = Mockito.mock(Restaurant.class);
        assertFalse(user.checkReserved(restaurant));
    }

    @Tag("Test if checkReservation returns False when the reservation is cancelled")
    @Test
    public void testCheckReservationReturnsFalseWhenCancelled() {
        Restaurant restaurant = Mockito.mock(Restaurant.class);
        Reservation reservation = Mockito.mock(Reservation.class);

        Mockito.when(reservation.isCancelled()).thenReturn(true);

        user.addReservation(reservation);
        assertFalse(user.checkReserved(restaurant));
    }

    @Tag("Test if checkReservation returns False when the reservation is in the future")
    @Test
    public void testCheckReservationReturnsFalseForFutureReservation() {
        Restaurant restaurant = Mockito.mock(Restaurant.class);
        Reservation reservation = Mockito.mock(Reservation.class);

        Mockito.when(reservation.getDateTime()).thenReturn(LocalDateTime.now().plusDays(1));

        user.addReservation(reservation);
        assertFalse(user.checkReserved(restaurant));
    }

}
