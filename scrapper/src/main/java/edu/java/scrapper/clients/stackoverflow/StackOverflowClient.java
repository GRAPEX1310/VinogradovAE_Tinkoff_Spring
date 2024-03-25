package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.model.StackOverflowResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<Optional<StackOverflowResponse>> getQuestionData(int questionId);
}
