/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.event;

/**
 *
 * @author ingri
 */
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// Mixin para agregar soporte de PropertyChange a cualquier clase
public class PropertyChangeSupportMixin {
    private final PropertyChangeSupport support;

    public PropertyChangeSupportMixin(Object sourceBean) {
        this.support = new PropertyChangeSupport(sourceBean);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }
}
