package com.amazon.ask.testcase.userinteraction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@Builder
public final class ResponseDialogAct {
    @JsonInclude(Include.NON_NULL)
    private String type;
    @JsonInclude(Include.NON_NULL)
    private Boolean success;
    @JsonInclude(Include.NON_NULL)
    private String actionName;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> arguments;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> carryoverArguments;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> requestArgs;
    @JsonInclude(Include.NON_NULL)
    private NextDialogAct nextDialogAct;

    @ConstructorProperties({"type", "success", "actionName", "arguments",
            "carryoverArguments", "requestArgs", "nextDialogAct"})
    ResponseDialogAct(String type, Boolean success, String actionName, List<String> arguments,
                      List<String> carryoverArguments, List<String> requestArgs, NextDialogAct nextDialogAct) {
        this.type = type;
        this.success = success;
        this.actionName = actionName;
        this.arguments = arguments;
        this.carryoverArguments = carryoverArguments;
        this.requestArgs = requestArgs;
        this.nextDialogAct = nextDialogAct;
    }


    public int hashCode() {
        boolean prime = true;
        int result = 1;
        Object type = this.getType();
        result = result * 59 + (type == null ? 43 : type.hashCode());
        Object success = this.getSuccess();
        result = result * 59 + (success == null ? 43 : success.hashCode());
        Object actionName = this.getActionName();
        result = result * 59 + (actionName == null ? 43 : actionName.hashCode());
        Object arguments = this.getArguments();
        result = result * 59 + (arguments == null ? 43 : arguments.hashCode());
        Object carryoverArguments = this.getCarryoverArguments();
        result = result * 59 + (carryoverArguments == null ? 43 : carryoverArguments.hashCode());
        Object requestArgs = this.getRequestArgs();
        result = result * 59 + (requestArgs == null ? 43 : requestArgs.hashCode());
        Object nextDialogAct = this.getNextDialogAct();
        result = result * 59 + (nextDialogAct == null ? 43 : nextDialogAct.hashCode());
        return result;
    }

    public String toString() {
        return "ResponseDialogAct(type=" + this.getType() + ", success="
                + this.getSuccess() + ", actionName="
                + this.getActionName() + ", arguments="
                + this.getArguments() + ", carryoverArguments="
                + this.getCarryoverArguments() + ", requestArgs="
                + this.getRequestArgs() + ", nextDialogAct=" + this.getNextDialogAct() + ")";
    }

}

