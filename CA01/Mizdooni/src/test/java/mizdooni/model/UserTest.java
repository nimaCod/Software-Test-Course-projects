package mizdooni.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    List<Reservation> reservations;
    @Mock Address address;
    @Mock User.Role role;
    @InjectMocks private User user;

    User create_user(){
        return new User("nima","password","nima@google.com",address,role);
    }
    @BeforeEach
    void setup(){
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

    @Tag("Test if getReservation that gets an ID works.")
    @Test
    public void testCheckReservation(){
        Reservation reservation = reservations.get(1);
        user.addReservation(reservation);
        int reservationNumber = reservation.getReservationNumber();
        assertEquals(reservation,user.getReservation(reservationNumber));
    }
}
