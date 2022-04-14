package com.amazon.ask.servlet.verifiers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.util.Objects;

@JsonDeserialize(
        builder = GenericTimestampRequest.Builder.class
)
public class GenericTimestampRequest {
    @JsonProperty("type")
    protected String type = null;
    @JsonProperty("requestId")
    protected String requestId = null;
    @JsonProperty("timestamp")
    protected OffsetDateTime timestamp = null;


    public static GenericTimestampRequest.Builder builder() {
        return new GenericTimestampRequest.Builder();
    }

    private GenericTimestampRequest(GenericTimestampRequest.Builder builder) {
        if (builder.requestId != null) {
            this.requestId = builder.requestId;
        }

        if (builder.timestamp != null) {
            this.timestamp = builder.timestamp;
        }

        if (builder.type != null) {
            this.type = builder.type;
        }
    }

    @JsonIgnore
    public String getType() {
        return this.type;
    }

    @JsonProperty("requestId")
    public String getRequestId() {
        return this.requestId;
    }

    @JsonProperty("timestamp")
    public OffsetDateTime getTimestamp() {
        return this.timestamp;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GenericTimestampRequest request = (GenericTimestampRequest) o;
            return Objects.equals(this.type, request.type) && Objects.equals(this.requestId, request.requestId) && Objects.equals(this.timestamp, request.timestamp);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.type, this.requestId, this.timestamp);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Request {\n");
        sb.append("    type: ").append(this.toIndentedString(this.type)).append("\n");
        sb.append("    requestId: ").append(this.toIndentedString(this.requestId)).append("\n");
        sb.append("    timestamp: ").append(this.toIndentedString(this.timestamp)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static class Builder {
        private String type;
        private String requestId;
        private OffsetDateTime timestamp;

        private Builder() {
        }

        @JsonProperty("requestId")
        public GenericTimestampRequest.Builder withRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @JsonProperty("timestamp")
        public GenericTimestampRequest.Builder withTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @JsonProperty("type")
        public GenericTimestampRequest.Builder withType(String type) {
            this.type = type;
            return this;
        }

        public GenericTimestampRequest build() {
            return new GenericTimestampRequest(this);
        }
    }
}