/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.labeledtree;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author huub
 * @param <Label>
 * @param <T>
 */
public class DotVisitor<Label, T extends LabeledTree<Label,T>> implements Visitor<Label, T> {    
    protected Map<LabeledTree,String> labels = new HashMap<>();
    protected int index = 0;
    final StringBuilder b;
    
    public DotVisitor(StringBuilder b) {
        this.b = b;
    }
    @Override
    public void visitNode(LabeledTree<Label, T> tree) {
        String label = Integer.toString(index++);
        labels.put(tree, label);
        String dotLabel = getLabelFor(tree,index);
        String shape = tree.isLeaf() ? "shape=\"box\"" : "";
        b.append(label).append(" [").append(shape);
        b.append(" label=\"").append(dotLabel).append("\"];\n");
    }
    
    public String getLabelFor(LabeledTree<Label,T> tree, int index) {
        return ""+index;
    }

    @Override
    public void visitEdge(LabeledTree<Label, T> parent, T child, Label label) {
        b.append(labels.get(parent)).append("->");
        b.append(labels.get(child)).append("[fontsize=8 label=\"");
        b.append(label.toString()).append("\"]\n");
    }
}