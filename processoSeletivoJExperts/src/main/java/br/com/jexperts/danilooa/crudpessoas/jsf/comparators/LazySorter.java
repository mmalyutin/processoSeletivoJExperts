package br.com.jexperts.danilooa.crudpessoas.jsf.comparators;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import br.com.jexperts.danilooa.crudpessoas.entity.Pessoa;

public class LazySorter implements Comparator<Pessoa> {
    
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    public int compare(Pessoa car1, Pessoa car2) {
        try {
            Object value1 = Pessoa.class.getField(this.sortField).get(car1);
            Object value2 = Pessoa.class.getField(this.sortField).get(car2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
