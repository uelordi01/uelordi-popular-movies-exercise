package udacity.uelordi.com.popularmovies.content;

/**
 * Created by uelordi on 26/04/17.
 */

public class APIError {

        private int statusCode;
        private String message;

        public APIError() {
        }

        public int status() {
            return statusCode;
        }

        public String message() {
            return message;
        }
    }

