package com.coinbasetraders.exception;

public class CoinbaseTradersRuntimeException extends RuntimeException {

    public CoinbaseTradersRuntimeException() {
        super();
    }

    public CoinbaseTradersRuntimeException(String message) {
        super(message);
    }

    public CoinbaseTradersRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoinbaseTradersRuntimeException(Throwable cause) {
        super(cause);
    }
}