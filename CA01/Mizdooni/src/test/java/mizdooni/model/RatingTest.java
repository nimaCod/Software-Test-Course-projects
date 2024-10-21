package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class RatingTest {

    private Rating rating;

    private Rating createRating(double overall){
        Rating rating = new Rating();
        rating.overall = overall;
        return rating;
    }

    @Tag("return rounded overall when it is lower than 5")
    @Test
    public void testGetStarCountRoundingOverallLowerThan5(){
        rating = createRating(3.5);
        assertEquals(4,rating.getStarCount());
    }

    @Tag("return 5 when overall > 5")
    @Test
    public void testGetStarCountReturning5WhenOverallHigherThan5(){
        rating = createRating(5.5);
        assertEquals(5,rating.getStarCount());    }

}
