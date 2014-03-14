/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.data.golf;

import nl.tue.s2id90.classification.data.Features;

/**
 *
 * @author huub
 */
public class GolfFeatures  implements Features {

    private final Object[] data;

    public GolfFeatures(Object[] data) {
        this.data = data;
    }

    @Override
    public Object get(int i) {
        return data[i];
    }

    @Override
    public int getNumberOfAttributes() {
        return data.length;
    }

    @Override
    public boolean isContinuous(int i) {
        return i == 1 || i == 2; // temperature and humidity
    }

    @Override
    public String getName(int i) {
        String[] names = {"Outlook", "Temperature", "Humidity", "Windy"};
        return names[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            sb.append(getName(i)).append("=").append(get(i)).append(' ');
        }
        return sb.toString();
    }
}
