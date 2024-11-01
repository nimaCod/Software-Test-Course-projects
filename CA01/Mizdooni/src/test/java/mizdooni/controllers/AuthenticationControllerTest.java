package mizdooni.controllers;

import jdk.jfr.Label;
import mizdooni.model.Address;
import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static mizdooni.controllers.ControllerUtils.PARAMS_BAD_TYPE;
import static mizdooni.controllers.ControllerUtils.PARAMS_MISSING;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private UserService userService = Mockito.mock(UserService.class);
    @InjectMocks
    private AuthenticationController authenticationController;

    private User get_valid_user(String username, String password, String email){
        Address address = Mockito.mock(Address.class);
        User.Role role = Mockito.mock(User.Role.class);
        return new User(username, password, email, address,role);

    }
    @Label("user")
    @Test
    public void given_valid_user_when_calling_user_then_responses_ok(){
        User user = get_valid_user("username","password","email");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        assertEquals( Response.ok("current user",user),authenticationController.user());
    }

    @Label("user")
    @Test
    public void given_non_existing_user_when_calling_user_then_throws_unauthorized_exception(){
        Mockito.when(userService.getCurrentUser()).thenReturn(null);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.user());
        assertEquals(HttpStatus.UNAUTHORIZED,exception.getStatus());
        assertEquals( "no user logged in", exception.getMessage());
    }

    @Label("login")
    @Test
    public void given_valid_userPass_when_login_then_logs_in_successful(){
        String username = "username";
        String password = "password";
        User user = get_valid_user(username,password,"email");
        Map<String,String> credentials = Map.of("username", username,"password", password );
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.login(username,password)).thenReturn(true);
        assertEquals(Response.ok("login successful",user),authenticationController.login(credentials));
    }

    @Label("login")
    @Test
    public void given_invalid_userPass_when_login_then_throws_bad_request_exception(){
        String username = "username";
        User user = get_valid_user(username,"password","email");
        Map<String,String> credentials = Map.of("username", username);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.login(credentials));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( PARAMS_MISSING, exception.getMessage());
    }

    @Label("login")
    @Test
    public void given_wrong_userPass_when_login_then_throws_unauthorized_exception(){
        String username = "username";
        String password = "password";
        User user = get_valid_user(username,password,"email");
        Map<String,String> credentials = Map.of("username", username,"password",password);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.login(credentials));
        assertEquals(HttpStatus.UNAUTHORIZED,exception.getStatus());
        assertEquals( "invalid username or password", exception.getMessage());
    }

    @Label("signup")
    @Test
    public void given_valid_type_of_parameters_when_signup_then_responses_ok(){
        String username = "ali123", password="password",email="email",role= "client",country="country",city="city";
        Map<String, String> address = Map.of("country",country,"city",city);
        Map<String,Object> credentials = Map.of("username", username,
                "password",password,
                "email",email,
                "role",role,
                "address",address);
        User user = get_valid_user(username,password,email);
        Mockito.when(userService.login(username,password)).thenReturn(true);
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        assertEquals(Response.ok("signup successful", user), authenticationController.signup(credentials));
    }

    @Label("signup")
    @Test
    public void given_insufficient_parameter_when_signup_then_throws_bad_request_exception(){
        String username = "username", password="password";
        Map<String,Object> credentials = Map.of("username", username,"password",password);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.signup(credentials));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( PARAMS_MISSING, exception.getMessage());
    }

    @Label("signup")
    @Test
    public void given_wrong_type_of_parameter_address_when_signup_then_throws_bad_request_exception_param_bad_type(){
        String username = "username", password="password",email="email",role="role",address="address";
        Map<String,Object> credentials = Map.of("username", username,
                "password",password,
                "email",email,
                "role",role,
                "address",address);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.signup(credentials));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( PARAMS_BAD_TYPE, exception.getMessage());
    }

    @Label("signup")
    @Test
    public void given_empty_type_of_parameter_username_when_signup_then_throws_bad_request_exception_param_missing(){
        String username = "", password="password",email="email",role= "client",country="country",city="city";
        Map<String, String> address = Map.of("country",country,"city",city);
        Map<String,Object> credentials = Map.of("username", username,
                "password",password,
                "email",email,
                "role",role,
                "address",address);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.signup(credentials));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( PARAMS_MISSING, exception.getMessage());
    }

    @Label("signup")
    @Test
    public void given_invalid_type_of_parameter_username_and_login_fails_when_signup_then_throws_bad_request_exception(){
        String username = "ali123!@", password="password",email="email",role= "client",country="country",city="city";
        Map<String, String> address = Map.of("country",country,"city",city);
        Map<String,Object> credentials = Map.of("username", username,
                "password",password,
                "email",email,
                "role",role,
                "address",address);
        Mockito.when(userService.login(username,password)).thenReturn(false);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.signup(credentials));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
    }

    @Label("logout")
    @Test
    public void given_logged_in_user_when_logging_out_then_logs_out_successfully(){
        Mockito.when(userService.logout()).thenReturn(true);
        assertEquals(Response.ok("logout successful"),authenticationController.logout());
    }

    @Label("logout")
    @Test
    public void given_unauthorized_user_when_logging_out_then_throws_unauthorized_exception(){
        Mockito.when(userService.logout()).thenReturn(false);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.logout());
        assertEquals(HttpStatus.UNAUTHORIZED,exception.getStatus());
        assertEquals( "no user logged in", exception.getMessage());
    }

// give list of invalid usernames in future!!! Farbod
    @Label("validateUsername")
    @Test
    public void given_invalid_username_when_validating_username_then_throws_bad_request_exception(){
        String invalid_username = "ali123@";
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.validateUsername(invalid_username));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( "invalid username format", exception.getMessage());
    }

// give list of valid usernames
    @Label("validateUsername")
    @Test
    public void given_existing_username_when_validating_username_then_throws_conflict_exception(){
        String valid_username = "ali123";
        Mockito.when(userService.usernameExists(valid_username)).thenReturn(true);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.validateUsername(valid_username));
        assertEquals(HttpStatus.CONFLICT,exception.getStatus());
        assertEquals( "username already exists", exception.getMessage());
    }

    @Label("validateUsername")
    @Test
    public void given_valid_username_when_validating_username_then_returns_ok(){
        String valid_username = "ali123";
        Mockito.when(userService.logout()).thenReturn(false);
        assertEquals(Response.ok("username is available"),authenticationController.validateUsername(valid_username));
    }


    // give list of invalid emails in future!!! Farbod
    @Label("validateEmail")
    @Test
    public void given_invalid_email_when_validating_email_then_throws_bad_request_exception(){
        String invalid_email = "ali123@";
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.validateEmail(invalid_email));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertEquals( "invalid email format", exception.getMessage());
    }

    // give list of valid emails
    @Label("validateEmail")
    @Test
    public void given_existing_email_when_validating_email_then_throws_conflict_exception(){
        String valid_email = "ali123@x.c";
        Mockito.when(userService.emailExists(valid_email)).thenReturn(true);
        ResponseException exception =  assertThrows(ResponseException.class,() -> authenticationController.validateEmail(valid_email));
        assertEquals(HttpStatus.CONFLICT,exception.getStatus());
        assertEquals( "email already registered", exception.getMessage());
    }

    @Label("validateEmail")
    @Test
    public void given_valid_email_when_validating_email_then_returns_ok(){
        String valid_email = "ali123@x.c";
        Mockito.when(userService.logout()).thenReturn(false);
        assertEquals(Response.ok("email not registered"),authenticationController.validateEmail(valid_email));
    }
}

