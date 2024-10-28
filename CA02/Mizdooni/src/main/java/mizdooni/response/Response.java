package mizdooni.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mizdooni.response.serializer.HttpStatusSerializer;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    @JsonSerialize(using = HttpStatusSerializer.class)
    private HttpStatus status;
    private boolean success;
    private String error;
    private String message;
    private Object data;

    public Response(HttpStatus status, String message, boolean success, String error, Object data) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.success = success;
        this.error = error;
        this.data = data;
    }

    public static Response ok(String message) {
        return new Response(HttpStatus.OK, message, true, null, null);
    }

    public static Response ok(String message, Object data) {
        return new Response(HttpStatus.OK, message, true, null, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Response response = (Response) obj;
        return success == response.success &&
//                Objects.equals(timestamp, response.timestamp) && // why they have different timestamps
                status == response.status &&
                Objects.equals(error, response.error) &&
                Objects.equals(message, response.message) &&
                Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, success, error, message, data);
    }
}
