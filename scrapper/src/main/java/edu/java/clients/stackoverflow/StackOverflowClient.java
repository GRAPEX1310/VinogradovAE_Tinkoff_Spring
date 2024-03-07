package edu.java.clients.stackoverflow;

import edu.java.model.StackOverflowResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<Optional<StackOverflowResponse>> getQuestionData(int questionId);
}
