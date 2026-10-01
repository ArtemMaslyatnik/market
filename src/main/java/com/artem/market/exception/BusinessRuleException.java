package com.artem.market.exception;

/**
 * Базовое исключение для нарушения бизнес-правил учетной системы.
 * Автоматически приводит к откату транзакций Spring Data JPA.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}