package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Tag("Test to check if idReserved works.")
    @Test
    public void testIsReserved(){
        fail();
    }
}
