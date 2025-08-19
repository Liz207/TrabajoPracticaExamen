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
public class PagoTransferencia implements Pago {
    private static final Random random = new Random();

    @Override
    public void procesar(BigDecimal monto) throws PagoRechazadoException {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoRechazadoException("El monto de la transferencia debe ser positivo.");
        }
        // Simular un 5% de posibilidad de fallo en la transferencia
        if (random.nextInt(20) == 0) {
            throw new PagoRechazadoException("Transferencia fallida. Intente nuevamente.");
        }
        System.out.println("Transferencia bancaria procesada por: " + monto);
    }
}
