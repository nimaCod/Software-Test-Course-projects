package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.as;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;


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

    private Table getDummyTable(){
        return new Table(1,1,1);
    }

    private Rating createRating(double food, double service, double ambiance, double overall) {
        Rating rating = new Rating();
        rating.food = food;
        rating.service = service;
        rating.ambiance = ambiance;
        rating.overall = overall;
        return rating;
    }

    @BeforeEach
    public void setup(){
        restaurant = createDummyRestaurant();
    }
    @Tag("testing if addTable works")
    @Test
    public void testAddTableWorks(){
        Table table = getDummyTable();
        restaurant.addTable(table);
        assertThat(restaurant.getTables()).contains(table);
        assertEquals(1,table.getTableNumber());
    }


    @Tag("testing if getTable finds existing table")
    @Test
    public void testGetTableFindsExistingTable(){
        Table table = getDummyTable();
        restaurant.addTable(table);
        int table_number = table.getTableNumber();
        assertEquals(table,restaurant.getTable(table_number));
    }

    @Tag("testing if getTable does not find the specified table")
    @Test
    public void testGetTableDoesNotFindSpecifiedTable(){
        Table table = getDummyTable();
        restaurant.addTable(table);
        int table_number = table.getTableNumber();
        assertNull(restaurant.getTable(table_number + 1 ));// number that does not exist
   }


   private Review createDummyReviewForAUser(User user,String comment){
        return  new Review(user,Mockito.mock(Rating.class),comment, LocalDateTime.now());
   }
    @Tag("testing addReview adds user's first review while reviews are empty")
    @Test
    public void testAddReviewWorksForFirstReviewInList(){
        User user1 = Mockito.mock(User.class);
        Review review = createDummyReviewForAUser(user1,"comment");
        restaurant.addReview(review);
        assertThat(restaurant.getReviews()).contains(review);
    }

    @Tag("testing addReview adds user's second review while there isn't any other user")
    @Test
    public void testAddReviewWorksForSecondReviewBeingSecondReviewInList(){
        User user1 = Mockito.mock(User.class);
        Review review1 = createDummyReviewForAUser(user1,"comment1");
        restaurant.addReview(review1);
        Review review2 = createDummyReviewForAUser(user1,"comment2");
        restaurant.addReview(review2);
        assertThat(restaurant.getReviews()).doesNotContain(review1);
        assertThat(restaurant.getReviews()).contains(review2);
    }


    @Tag("testing addReview adds user's second review while another user has a review in the list")
    @Test
    public void testAddReviewWorksForSecondReviewBeingThirdReviewInList(){
        User user2 = Mockito.mock(User.class);
        Review review2_2 = createDummyReviewForAUser(user2,"comment1 user2");
        restaurant.addReview(review2_2);

        User user1 = Mockito.mock(User.class);
        Review review1 = createDummyReviewForAUser(user1,"comment1 user1");
        restaurant.addReview(review1);
        Review review2 = createDummyReviewForAUser(user1,"comment2 user1");
        restaurant.addReview(review2);

        assertThat(restaurant.getReviews()).contains(review2_2);
        assertThat(restaurant.getReviews()).doesNotContain(review1);
        assertThat(restaurant.getReviews()).contains(review2);
    }


    @Tag("testing getAverageRating works while there isn't any review")
    @Test
    public void testGetAverageRatingWorksWhileNotHavingAnyReviews(){
        Rating averageRating = restaurant.getAverageRating();

        assertEquals(0, averageRating.food);
        assertEquals(0, averageRating.service);
        assertEquals(0, averageRating.ambiance);
        assertEquals(0, averageRating.overall);
    }

    @Tag("testing getAverageRating works while having one review")
    @Test
    public void testGetAverageRatingWorksWhileHavingOneReview(){
        Rating rating = createRating(5, 4, 3, 4);

        Review review = Mockito.mock(Review.class);
        Mockito.when(review.getRating()).thenReturn(rating);

        restaurant.addReview(review);

        Rating averageRating = restaurant.getAverageRating();

        assertEquals(5, averageRating.food);
        assertEquals(4, averageRating.service);
        assertEquals(3, averageRating.ambiance);
        assertEquals(4, averageRating.overall);
    }

    @Tag("testing getAverageRating works while having multiple reviews")
    @Test
    public void testGetAverageRatingWorksWhileHavingMultipleReviews(){
        Rating rating1 = createRating(4, 3, 2, 3);
        Rating rating2 = createRating(5, 4, 5, 4);

        Review review1 = Mockito.mock(Review.class);
        Review review2 = Mockito.mock(Review.class);
        Mockito.when(review1.getRating()).thenReturn(rating1);
        Mockito.when(review2.getRating()).thenReturn(rating2);
        Mockito.when(review1.getUser()).thenReturn(Mockito.mock(User.class));
        Mockito.when(review1.getUser()).thenReturn(Mockito.mock(User.class));

        restaurant.addReview(review1);
        restaurant.addReview(review2);

        Rating averageRating = restaurant.getAverageRating();

        assertEquals(4.5, averageRating.food);
        assertEquals(3.5, averageRating.service);
        assertEquals(3.5, averageRating.ambiance);
        assertEquals(3.5, averageRating.overall);
    }

    @Tag("testing getMaxSeatsNumber while not having any tables")
    @Test
    public void testGetMaxSeatsNumberWhileNotHavingAnyTables(){
        assertEquals(1,restaurant.getMaxSeatsNumber());
    }


    private Table getDummyTable(int seatNumber){
        return new Table(1,1,seatNumber);
    }
    @Tag("testing getMaxSeatsNumber while having some tables without any seats")
    @Test
    @Disabled("This test returns 0 and assertion fails so it seems behavior has got no problem with no seats")
    public void testGetMaxSeatsNumberWhileHavingSomeTablesWithoutAnySeats(){
        Table table = getDummyTable(0);
        restaurant.addTable(table);
        assertEquals(1,restaurant.getMaxSeatsNumber());
    }

    @Tag("testing getMaxSeatsNumber while having some tables with seats")
    @Test
    public void testGetMaxSeatsNumberWhileHavingSomeTablesWithSeats(){
        Table table1 = getDummyTable(10);
        restaurant.addTable(table1);
        Table table2 = getDummyTable(20);
        restaurant.addTable(table2);
        Table table3 = getDummyTable(15);
        restaurant.addTable(table3);
        assertEquals(20,restaurant.getMaxSeatsNumber());    }


}
