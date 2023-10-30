package com.example.myassignment1.employeemanagementsystem.exception;

public class DeleteNotSuccessException extends RuntimeException{
    public DeleteNotSuccessException(String message){
        super(message);
    }
}
