package com.amazon.ask.testcase.userinteraction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.beans.ConstructorProperties;
import java.util.List;

public final class NextDialogAct {
    @JsonInclude(Include.NON_NULL)
    private String type;
    @JsonInclude(Include.NON_NULL)
    private String actionName;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> arguments;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> carryoverArguments;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> requestArgs;

    @ConstructorProperties({"type", "actionName", "arguments", "carryoverArguments", "requestArgs"})
    NextDialogAct(String type, String actionName, List<String> arguments,
                  List<String> carryoverArguments, List<String> requestArgs) {
        this.type = type;
        this.actionName = actionName;
        this.arguments = arguments;
        this.carryoverArguments = carryoverArguments;
        this.requestArgs = requestArgs;
    }

    public static NextDialogActBuilder builder() {
        return new NextDialogActBuilder();
    }

    public String getType() {
        return this.type;
    }

    public String getActionName() {
        return this.actionName;
    }

    public List<String> getArguments() {
        return this.arguments;
    }

    public List<String> getCarryoverArguments() {
        return this.carryoverArguments;
    }

    public List<String> getRequestArgs() {
        return this.requestArgs;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void setCarryoverArguments(List<String> carryoverArguments) {
        this.carryoverArguments = carryoverArguments;
    }

    public void setRequestArgs(List<String> requestArgs) {
        this.requestArgs = requestArgs;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NextDialogAct)) {
            return false;
        } else {
            NextDialogAct other = (NextDialogAct) o;
            Object thisType = this.getType();
            Object otherType = other.getType();
            if (thisType == null) {
                if (otherType != null) {
                    return false;
                }
            } else if (!thisType.equals(otherType)) {
                return false;
            }

            label61: {
                Object thisactionName = this.getActionName();
                Object otheractionName = other.getActionName();
                if (thisactionName == null) {
                    if (otheractionName == null) {
                        break label61;
                    }
                } else if (thisactionName.equals(otheractionName)) {
                    break label61;
                }

                return false;
            }

            label54: {
                Object thisarguments = this.getArguments();
                Object otherarguments = other.getArguments();
                if (thisarguments == null) {
                    if (otherarguments == null) {
                        break label54;
                    }
                } else if (thisarguments.equals(otherarguments)) {
                    break label54;
                }

                return false;
            }

            Object thiscarryoverArguments = this.getCarryoverArguments();
            Object othercarryoverArguments = other.getCarryoverArguments();
            if (thiscarryoverArguments == null) {
                if (othercarryoverArguments != null) {
                    return false;
                }
            } else if (!thiscarryoverArguments.equals(othercarryoverArguments)) {
                return false;
            }

            Object thisrequestArgs = this.getRequestArgs();
            Object otherrequestArgs = other.getRequestArgs();
            if (thisrequestArgs == null) {
                if (otherrequestArgs != null) {
                    return false;
                }
            } else if (!thisrequestArgs.equals(otherrequestArgs)) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        boolean prime = true;
        int result = 1;
        Object type = this.getType();
        result = result * 59 + (type == null ? 43 : type.hashCode());
        Object actionName = this.getActionName();
        result = result * 59 + (actionName == null ? 43 : actionName.hashCode());
        Object arguments = this.getArguments();
        result = result * 59 + (arguments == null ? 43 : arguments.hashCode());
        Object carryoverArguments = this.getCarryoverArguments();
        result = result * 59 + (carryoverArguments == null ? 43 : carryoverArguments.hashCode());
        Object requestArgs = this.getRequestArgs();
        result = result * 59 + (requestArgs == null ? 43 : requestArgs.hashCode());
        return result;
    }

    public String toString() {
        return "NextDialogAct(type=" + this.getType()
                + ", actionName=" + this.getActionName()
                + ", arguments=" + this.getArguments()
                + ", carryoverArguments=" + this.getCarryoverArguments()
                + ", requestArgs=" + this.getRequestArgs() + ")";
    }

    public static class NextDialogActBuilder {
        private String type;
        private String actionName;
        private List<String> arguments;
        private List<String> carryoverArguments;
        private List<String> requestArgs;

        NextDialogActBuilder() {
        }

        public NextDialogActBuilder type(String type) {
            this.type = type;
            return this;
        }

        public NextDialogActBuilder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public NextDialogActBuilder arguments(List<String> arguments) {
            this.arguments = arguments;
            return this;
        }

        public NextDialogActBuilder carryoverArguments(List<String> carryoverArguments) {
            this.carryoverArguments = carryoverArguments;
            return this;
        }

        public NextDialogActBuilder requestArgs(List<String> requestArgs) {
            this.requestArgs = requestArgs;
            return this;
        }

        public NextDialogAct build() {
            return new NextDialogAct(this.type,
                    this.actionName,
                    this.arguments,
                    this.carryoverArguments,
                    this.requestArgs);
        }

        public String toString() {
            return "NextDialogAct.NextDialogActBuilder(type=" + this.type
                    + ", actionName=" + this.actionName
                    + ", arguments=" + this.arguments
                    + ", carryoverArguments=" + this.carryoverArguments
                    + ", requestArgs=" + this.requestArgs + ")";
        }
    }
}
