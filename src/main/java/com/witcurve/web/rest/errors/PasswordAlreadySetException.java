package com.witcurve.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class PasswordAlreadySetException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public PasswordAlreadySetException() {
        super(ErrorConstants.PASSWORD_ALREADY_SET, "Password already exists", Status.BAD_REQUEST);
    }
}
