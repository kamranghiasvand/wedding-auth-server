package com.bluebox.planner.auth.rest.controller.permission;

import com.bluebox.planner.auth.common.config.InputProperties;
import com.bluebox.planner.auth.common.viewModel.permission.PermissionDto;
import com.bluebox.planner.auth.common.viewModel.regular.RegularUserDto;
import com.bluebox.planner.auth.rest.validation.ValidationFactory;
import com.bluebox.planner.auth.validators.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.bluebox.planner.auth.common.Constants.*;
import static com.bluebox.planner.auth.validators.RuleFactory.*;
import static java.text.MessageFormat.format;

/**
 * @author by kamran ghiasvand
 */
@Service
public class PermissionValidationFactory extends ValidationFactory<PermissionDto, Long> {

    @Autowired
    public PermissionValidationFactory(InputProperties inputProperties) {
        super(inputProperties);

    }

    @Override
    protected void init() {
        var nullMethod = nullRule(PermissionDto::getMethod, FIELD_PERMISSION_METHOD);
        var name = nullEmptyLenRule(PermissionDto::getName, FIELD_PERMISSION_NAME, inputProps.getTextMaxLength());
        var url = nullEmptyLenRule(PermissionDto::getUrl, FIELD_PERMISSION_URL, inputProps.getTextMaxLength());

        createCtx.addAll(nullMethod,
                name.getNullRule(),name.getEmptyRule(),name.getLenRule(),
                url.getNullRule(),url.getEmptyRule(),url.getLenRule());
        updateCtx.addAll(name.getEmptyRule(),name.getLenRule(),
                url.getEmptyRule(),url.getLenRule());
    }
}
