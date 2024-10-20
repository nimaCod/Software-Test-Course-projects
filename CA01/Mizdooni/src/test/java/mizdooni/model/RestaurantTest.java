package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(MockitoExtension.class)
public class RestaurantTest {

    private Restaurant restaurant;

    private Restaurant createDummyRestaurant(){
        User manager = Mockito.mock(User.class);
        Address address = Mockito.mock(Address.class);
        restaurant = new Restaurant("name", manager, "type", LocalTime.of(7, 10, 19), LocalTime.of(23, 10, 20),
                "description", address, "link");

        return restaurant;
    }


    @Tag("testing if addTable works")
    @Test
    public void testAddTableWorks(){
        fail();
    }


    @Tag("testing if getTable finds existing table")
    @Test
    public void testGetTableFindsExistingTable(){
        fail();
    }

    @Tag("testing if getTable does not find the specified table")
    @Test
    public void testGetTableDoesNotFindSpecifiedTable(){
        fail();
    }

    @Tag("testing addReview adds user's first review while reviews are empty")
    @Test
    public void testAddReviewWorksForFirstReviewInList(){
        fail();
    }

    @Tag("testing addReview adds user's second review while there isn't any other user")
    @Test
    public void testAddReviewWorksForSecondReviewBeingSecondReviewInList(){
        fail();
    }


    @Tag("testing addReview adds user's second review while another user has a review in the list")
    @Test
    public void testAddReviewWorksForSecondReviewBeingThirdReviewInList(){
        fail();
    }


    @Tag("testing getAverageRating works while there isn't any review")
    @Test
    public void testGetAverageRatingWorksWhileNotHavingAnyReviews(){
        fail();
    }

    @Tag("testing getAverageRating works while having one review")
    @Test
    public void testGetAverageRatingWorksWhileHavingOneReview(){
        fail();
    }

    @Tag("testing getAverageRating works while having multiple reviews")
    @Test
    public void testGetAverageRatingWorksWhileHavingMultipleReviews(){
        fail();
    }

    @Tag("testing getMaxSeatsNumber while not having any tables")
    @Test
    public void testGetMaxSeatsNumberWhileNotHavingAnyTables(){
        fail();
    }

    @Tag("testing getMaxSeatsNumber while having some tables without any seats")
    @Test
    public void testGetMaxSeatsNumberWhileHavingSomeTablesWithoutAnySeats(){
        fail();
    }

    @Tag("testing getMaxSeatsNumber while having some tables with seats")
    @Test
    public void testGetMaxSeatsNumberWhileHavingSomeTablesWithSeats(){
        fail();
    }


}
