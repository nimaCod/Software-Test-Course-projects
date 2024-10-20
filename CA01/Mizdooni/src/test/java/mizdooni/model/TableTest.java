package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TableTest {
    List<Reservation> reservations;
    private Table table;

    private Table getDummyTable(){
        return new Table(1,1,1);
    }

    @BeforeEach
    void setup(){
        reservations = Arrays.asList(
                Mockito.mock(Reservation.class),
                Mockito.mock(Reservation.class)
        );
        table = getDummyTable();
    }

    @Tag("Test to check if the AddReservation works.")
    @Test
    public void testAddReservation(){
        Reservation reservation = reservations.get(0);
        table.addReservation(reservation);
        assertThat(table.getReservations()).contains(reservation);
    }

    @Tag("Test to check if isReserved works when there is no reservation.")
    @Test
    public void testIsReservedWhenNoReservationExists(){
        assertFalse(table.isReserved(LocalDateTime.now()));
    }

    @Tag("Test to check if isReserved works when there is a reservation.")
    @Test
    public void testIsReservedWhenReservationExists(){
        Mockito.when(reservations.get(0).getDateTime()).thenReturn(LocalDateTime.of(2024, 10, 20, 12, 0));
        Reservation reservation = reservations.get(0);
        table.addReservation(reservation);
        assertTrue(table.isReserved(reservation.getDateTime()));
    }

    @Tag("Test to check if isReserved works when there is a reservation but is cancelled")
    @Test
    public void testIsReservedWhenReservationExistsButCancelled(){
        Mockito.when(reservations.get(0).getDateTime()).thenReturn(LocalDateTime.of(2024, 10, 20, 12, 0));
        Mockito.when(reservations.get(0).isCancelled()).thenReturn(Boolean.TRUE);
        Reservation reservation = reservations.get(0);
        table.addReservation(reservation);
        assertFalse(table.isReserved(reservation.getDateTime()));
    }
}

// bug detected: there could be reservations more than number of seats in a datetime