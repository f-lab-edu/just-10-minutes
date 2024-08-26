package com.flab.just_10_minutes.Util.Exception.Database;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends DatabaseException {

    public static final String NOT_FOUND = "Not Found %s";
    public static final String USER = "User";
    public static final String POINT_HISTORY = "Point History";
    public static final String IMP_UID = "ImpUid";
    public static final String ORDER = "Order";
    public static final String PRODUCT = "Product";


    public NotFoundException(String message, String target) {
        super(String.format(message, target), HttpStatus.NOT_FOUND);
    }
}
