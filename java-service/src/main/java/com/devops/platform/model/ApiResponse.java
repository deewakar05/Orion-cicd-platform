package com.devops.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/**
 * ApiResponse<T>
 *
 * Generic, type-safe wrapper for all REST API responses.
 * Ensures consistent JSON structure across all endpoints.
 *
 * @param <T> the data payload type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final String timestamp;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }

    /** Factory method for successful responses. */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /** Factory method for error responses. */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess()   { return success;   }
    public String  getMessage()  { return message;   }
    public T       getData()     { return data;      }
    public String  getTimestamp(){ return timestamp; }
}
