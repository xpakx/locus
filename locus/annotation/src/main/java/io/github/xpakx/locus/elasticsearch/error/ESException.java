package io.github.xpakx.locus.elasticsearch.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ESException extends RuntimeException {
    public ESException(String message) {
        super(message);
    }

    public ESException() {
        super("Cannot connect to Elasticsearch!");
    }
}