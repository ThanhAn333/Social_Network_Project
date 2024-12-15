package Zabook.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FriendshipStatus {
	PENDING("pending"),
    ACCEPTED("friend"),
    REJECTED("rejected"),
    BLOCKED("blocked"),
    NONE("none");

    private final String value;

    FriendshipStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
