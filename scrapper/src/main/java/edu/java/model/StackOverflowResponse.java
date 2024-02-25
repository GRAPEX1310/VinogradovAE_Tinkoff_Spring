package edu.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public record StackOverflowResponse(
    @JsonProperty("question_id") int questionId,
    @JsonProperty("last_activity_date") long lastActivityDateUnixFormat
) {
    public OffsetDateTime getLastActivityDate() {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDateUnixFormat), ZoneId.systemDefault());
    }

    public record StackOverflowItemResponseList(
        @JsonProperty("items") List<StackOverflowResponse> itemsList
    ) {
    }
}
