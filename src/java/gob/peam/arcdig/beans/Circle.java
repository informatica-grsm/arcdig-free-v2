/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.beans;

/**
 *
 * @author Cj.Legacy
 */
public class Circle {
    private String label;
    private Integer data;
    private String color;

    public Circle() {
    }

    public Circle(String label, Integer data, String color) {
        this.label = label;
        this.data = data;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
}
