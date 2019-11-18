package com.someexp.parking_info.exception;

import javax.servlet.http.HttpServletRequest;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.util.Result;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GloabalExceptionHandler {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private void loggerUser(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null)
            logger.error(MagicVariable.LOGGER_USER + user.getId());
    }

    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) {
        loggerUser(req);
        logger.error(MagicVariable.UNKNOW_ERROR, e);
        return Result.fail(MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public Object numberFormatExceptionErrorHandler(HttpServletRequest req, NumberFormatException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_NUMBER_FORMAT_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_NUMBER_FORMAT_EXCEPTION + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Object numberFormatExceptionErrorHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = NoSuchFieldException.class)
    public Object NoSuchFieldException(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = IllegalAccessException.class)
    public Object IllegalAccessException(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public Object DataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
    public Object MySQLIntegrityConstraintViolationException(HttpServletRequest req, MySQLIntegrityConstraintViolationException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION +
                MagicVariable.LOGGER_ERROR_MESSAGE);
    }


}