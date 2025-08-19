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

public interface Pago {
    void procesar(BigDecimal monto) throws PagoRechazadoException;
}
