package com.mukesh.internCapstoneProject.exception;

public class ExceptionMessages {
    private ExceptionMessages() { }

    public static final String USER_WITH_USERNAME_EXISTS = "User with specified username already exists.";
    public static final String USER_WITH_EMAIL_EXISTS = "User with specified email already exists.";

    public static final String USER_LOGIN_MESSAGE = "The accessToken will be active for the next 10min. Once the accessToken is expired, user can regenerate the accessToken using the issued refreshToken which will be valid for the next 24hrs.";
    public static final String USER_UNAUTHORIZED_EXCEPTION = "User is not authenticated, please verify the credentials entered and try again.";

    public static final String BEARER_TOKEN_EXCEPTION = "{\"errorMessage\":\"Please pass the Access Token.\"}";
    public static final String TASK_NOT_FOUND_EXCEPTION = "Task with specified ID does not exist.";
    public static final String TASK_ALREADY_EXISTS = "Task with specified taskType already exists. Please re-confirm the taskType.";
}
