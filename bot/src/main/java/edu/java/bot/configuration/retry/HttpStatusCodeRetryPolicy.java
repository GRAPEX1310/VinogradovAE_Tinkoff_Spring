package edu.java.bot.configuration.retry;

import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Set;

public class HttpStatusCodeRetryPolicy extends SimpleRetryPolicy {
    private final Set<Integer> retryStatusCodes;

    public HttpStatusCodeRetryPolicy(Integer maxAttempts, Set<Integer> retryStatusCodes) {
        super(maxAttempts);
        this.retryStatusCodes = retryStatusCodes;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        if (context.getLastThrowable() instanceof HttpClientErrorException e) {
            return retryStatusCodes.contains(e.getStatusCode().value());
        }

        return super.canRetry(context);
    }
}
