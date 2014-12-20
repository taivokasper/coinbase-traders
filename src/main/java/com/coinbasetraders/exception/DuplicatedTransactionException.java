package com.coinbasetraders.exception;


public class DuplicatedTransactionException extends CoinbaseTradersRuntimeException {

    public DuplicatedTransactionException() {
        super();
    }

    public DuplicatedTransactionException(String message) {
        super(message);
    }

    public DuplicatedTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedTransactionException(Throwable cause) {
        super(cause);
    }
}