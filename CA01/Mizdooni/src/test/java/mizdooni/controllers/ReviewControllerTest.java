package mizdooni.controllers;

import jdk.jfr.Label;


import mizdooni.model.*;

import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;


import java.time.LocalTime;

import java.util.HashMap;

import java.util.Map;

import static mizdooni.controllers.ControllerUtils.PARAMS_MISSING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {


    private RestaurantService restaurantService = Mockito.mock(RestaurantService.class);

    private ReviewService reviewService = Mockito.mock(ReviewService.class);

    @InjectMocks
    private ReviewController reviewController;

    @Label("Restaurant does not exist")
    @Test
    public void given_invalid_restaurant_id_when_getting_reviews_then_responses_not_found(){
        ResponseException exception =  assertThrows(ResponseException.class,() -> reviewController.getReviews(1, 1));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }


    @Label("Successfully adding a review using addReview method")
    @Test
    public void given_valid_parameters_when_adding_review_then_returns_successful_response(){
        Mockito.when(restaurantService.getRestaurant(1)).thenReturn(new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link"));

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "Great restaurant!");
        Map<String, Double> rating = Map.of("food", 4.5, "service", 4.0, "ambiance", 5.0, "overall", 4.5);
        params.put("rating", rating);

        Response response = reviewController.addReview(1, params);

        assertEquals(response, new Response(HttpStatus.OK, "review added successfully", true, null, null));

    }


}
