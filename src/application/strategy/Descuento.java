/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.strategy;
import domain.Orden;
import java.math.BigDecimal;
/**
 *
 * @author ingri
 */
public interface Descuento {
    BigDecimal aplicar(Orden orden);
}
