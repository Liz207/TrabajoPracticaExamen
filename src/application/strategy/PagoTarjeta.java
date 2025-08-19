/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.strategy;
import application.exception.PagoRechazadoException;
import java.math.BigDecimal;
import java.util.Random;
/**
 *
 * @author ingri
 */
public class PagoTarjeta implements Pago {
    private static final Random random = new Random();

    @Override
    public void procesar(BigDecimal monto) throws PagoRechazadoException {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoRechazadoException("El monto del pago con tarjeta debe ser positivo.");
        }
        // Simular un 10% de posibilidad de rechazo
        if (random.nextInt(10) == 0) {
            throw new PagoRechazadoException("Pago con tarjeta rechazado por el banco.");
        }
        System.out.println("Pago con tarjeta procesado por: " + monto);
    }
}
