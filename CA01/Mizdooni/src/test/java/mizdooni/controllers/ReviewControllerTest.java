package mizdooni.controllers;

import jdk.jfr.Label;


import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.model.*;

import mizdooni.response.PagedList;
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

import java.util.List;
import java.util.Map;

import static mizdooni.controllers.ControllerUtils.PARAMS_BAD_TYPE;
import static mizdooni.controllers.ControllerUtils.PARAMS_MISSING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {


    private RestaurantService restaurantService = Mockito.mock(RestaurantService.class);

    private ReviewService reviewService = Mockito.mock(ReviewService.class);

    @InjectMocks
    private ReviewController reviewController;

    @Label("getting not found error for getReviews when Restaurant does not exist")
    @Test
    public void given_invalid_restaurant_id_when_getting_reviews_then_responses_not_found(){
        ResponseException exception =  assertThrows(ResponseException.class,() -> reviewController.getReviews(1, 1));
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatus());
    }

    @Label("getting 200 response (with empty data) for getReviews when there is no review but restaurant exists")
    @Test
    public void given_valid_restaurant_id_with_no_reviews_when_getting_reviews_then_returns_empty_list() {
        Mockito.when(restaurantService.getRestaurant(1)).thenReturn(new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link"));
        try {
            Mockito.when(reviewService.getReviews(1, 1)).thenReturn(new PagedList<>(List.of(), 1, 10));
        } catch (RestaurantNotFound e) {
            throw new RuntimeException(e);
        }

        Response response = reviewController.getReviews(1, 1);
        assertEquals(response, new Response(HttpStatus.OK, "reviews for restaurant (" + 1 + "): " + "name", true, null, null));
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

    @Label("trying to add a review with missing param causing error")
    @Test
    public void given_missing_param_when_adding_review_then_throws_bad_request(){
        Mockito.when(restaurantService.getRestaurant(1)).thenReturn(new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link"));

        Map<String, Object> params = new HashMap<>();
        Map<String, Double> rating = Map.of("food", 4.5, "service", 4.0, "ambiance", 5.0, "overall", 4.5);
        params.put("rating", rating);

        ResponseException exception = assertThrows(ResponseException.class, () -> reviewController.addReview(1, params));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PARAMS_MISSING, exception.getMessage());

        params.clear();
        params.put("comment", "this is a comment");

        exception = assertThrows(ResponseException.class, () -> reviewController.addReview(1, params));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PARAMS_MISSING, exception.getMessage());
    }

    @Label("in addReview() having bad request error because of invalid rating values")
    @Test
    public void given_invalid_rating_values_when_adding_review_then_throws_bad_request(){
        Mockito.when(restaurantService.getRestaurant(1)).thenReturn(new Restaurant("name", Mockito.mock(User.class), "", LocalTime.of(1,1,1), LocalTime.now(), "address", Mockito.mock(Address.class), "link"));

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "this is a comment");
        Map<String, String> rating = Map.of("food", "5.0", "service", "4.0", "ambiance", "5.0", "overall", "4.5");
        params.put("rating", rating);

        ResponseException exception = assertThrows(ResponseException.class, () -> reviewController.addReview(1, params));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PARAMS_BAD_TYPE, exception.getMessage());
    }

    @Label("getting not found error for addReview when restaurant does not exist")
    @Test
    public void given_non_existent_restaurant_id_when_adding_review_then_responses_not_found() {
        Map<String, Object> params = new HashMap<>();
        params.put("comment", "this is comment");
        Map<String, Double> rating = Map.of("food", 4.5, "service", 4.0, "ambiance", 5.0, "overall", 4.5);
        params.put("rating", rating);

        ResponseException exception = assertThrows(ResponseException.class, () -> reviewController.addReview(2, params));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
