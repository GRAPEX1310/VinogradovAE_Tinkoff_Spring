package edu.java.dto.response;

import java.util.List;

public record ClientErrorResponse(String description, String code, String exceptionName,
                                  String exceptionMessage, List<String> stacktrace) {
}
