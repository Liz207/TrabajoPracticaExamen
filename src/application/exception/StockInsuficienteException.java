package application.exception;

/** Una orden no puede reservar más unidades que las disponibles. */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
