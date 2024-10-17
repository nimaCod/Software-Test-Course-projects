package mizdooni.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    Reservation reservation = Mockito.mock(Reservation.class);
    @Mock Address address;
    @Mock User.Role role;
    @InjectMocks private User user;

    User create_user(){
        return new User("nima","password","nima@google.com",address,role);
    }
    @BeforeEach
    void setup(){
        user = create_user();

    }

    @Test
    public void testAddReservation(){
        user.addReservation(reservation);
        System.out.println(user.getReservations());
        assertTrue(user.getReservations().contains(reservation));
        int id = reservation.getReservationNumber();
        assertEquals(0,id);
    }
}
