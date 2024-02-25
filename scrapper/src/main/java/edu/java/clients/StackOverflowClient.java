package edu.java.clients;

import edu.java.model.StackOverflowResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<Optional<StackOverflowResponse>> getQuestionData(int questionId);
}
