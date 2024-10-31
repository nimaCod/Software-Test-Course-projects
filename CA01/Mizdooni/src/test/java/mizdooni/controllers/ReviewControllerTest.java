package mizdooni.controllers;

import jdk.jfr.Label;
import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {


    private RestaurantService restaurantService = Mockito.mock(RestaurantService.class);

    private ReviewService reviewService = Mockito.mock(ReviewService.class);

    @InjectMocks
    private ReviewController reviewController;

    @Label("Restaurant does not exist")
    @Test
    public void given_invalid_restaurant_id_when_getting_reviews_then_responses_bad_request(){
        ResponseException exception =  assertThrows(ResponseException.class,() -> reviewController.getReviews(1, 1));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }
}
