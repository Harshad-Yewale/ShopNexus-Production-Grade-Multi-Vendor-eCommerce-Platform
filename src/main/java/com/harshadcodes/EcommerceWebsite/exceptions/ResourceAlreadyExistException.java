package com.harshadcodes.EcommerceWebsite.exceptions;

public class ResourceAlreadyExistException extends RuntimeException{
    String resourceName;
    String field;
    String fieldName;
    long fieldId;

    public ResourceAlreadyExistException(String resourceName, String field, String fieldName) {
        super(String.format("%s Already Exist with %s: %s",resourceName,field,fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceAlreadyExistException(String resourceName, String field, long fieldId) {
        super(String.format("%s Already with %s: %s",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
