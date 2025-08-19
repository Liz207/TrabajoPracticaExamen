/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.strategy;

/**
 *
 * @author ingri
 */

import application.exception.PagoRechazadoException;
import java.math.BigDecimal;

public class PagoEfectivo implements Pago {
    @Override
    public void procesar(BigDecimal monto) throws PagoRechazadoException {
        // En efectivo, siempre se acepta si el monto es positivo
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoRechazadoException("El monto del pago en efectivo debe ser positivo.");
        }
        System.out.println("Pago en efectivo procesado por: " + monto);
    }
}
