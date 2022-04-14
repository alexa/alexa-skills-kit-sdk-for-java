package com.amazon.ask.servlet.verifiers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;

@JsonDeserialize(
        builder = GenericTimestampRequestEnvelope.Builder.class
)
public final class GenericTimestampRequestEnvelope {
    @JsonProperty("request")
    private GenericTimestampRequest request;

    public static GenericTimestampRequestEnvelope.Builder builder() {
        return new GenericTimestampRequestEnvelope.Builder();
    }

    private GenericTimestampRequestEnvelope(GenericTimestampRequestEnvelope.Builder builder) {
        this.request = null;

        if (builder.request != null) {
            this.request = builder.request;
        }

    }

    @JsonProperty("request")
    public GenericTimestampRequest getRequest() {
        return this.request;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GenericTimestampRequestEnvelope requestEnvelope = (GenericTimestampRequestEnvelope)o;
            return Objects.equals(this.request, requestEnvelope.request);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.request);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RequestEnvelope {\n");
        sb.append("    request: ").append(this.toIndentedString(this.request)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static class Builder {
        private GenericTimestampRequest request;

        private Builder() {
        }

        @JsonProperty("request")
        public GenericTimestampRequestEnvelope.Builder withRequest(GenericTimestampRequest request) {
            this.request = request;
            return this;
        }

        public GenericTimestampRequestEnvelope build() {
            return new GenericTimestampRequestEnvelope(this);
        }
    }
}
