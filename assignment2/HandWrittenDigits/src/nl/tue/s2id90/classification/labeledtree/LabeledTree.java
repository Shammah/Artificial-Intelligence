/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.labeledtree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class representing a tree with labels on the edges. This actually represents
 * the root node of this tree and gives access to children of this node via labels.
 * Duplicate labels are not allowed. See class <code>DemoTree</code> for a simple
 * example on how to use this class.
 * @author huub
 * @param <Label> type of edge label
 * @param <T> type of child trees
 * @see DemoTree
 */
public class LabeledTree<Label,T extends LabeledTree<Label,T>> { 
    private final Map<Label,T> children = new HashMap<>();
  
    /**
     * @param label
     * @return the sub tree with this label; null, if such a  tree does not exist.
     */
    public T getSubTree(Label label) {
        return children.get(label);
    }
    
    /** @return whether or not the root of this tree has children. **/
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /** @return the labels of the edges in the root of this tree;
     *           null, if this root has no children.
     **/
    public Set<Label> getLabels() {
        if (isLeaf()) {
            return null;
        } else {
            return children.keySet();
        }
    }
    
    /** @return the sub trees of the root of this tree;
     *           null, if this root has no children.
     **/
    public Collection<T> getSubTrees() {
        if (isLeaf()) {
            return null;
        } else {
            return children.values();
        }
    }
    
    /** adds a labeled subtree to the root of this tree; if the label already
     * exists the original tree is replaced.
     * @param label
     * @param tree
     **/
    public void addSubTree(Label label, T tree) {
        children.put(label, tree);
    }
    
    /** uses the visitor v to walk the tree in a depth-first pre-order fashion: that is recursively
     * first the root of the tree is visited, and then one-by-one all it's sub trees. The order of 
     * visiting is shown in the following image:
     * <br><img src="doc-files/demotree.png">.
     * 
     * @param v  visitor
     * @see Visitor
     */
    public void depthFirstPreOrderVisit(Visitor<Label,T> v) {
        v.visitNode(this);
        if (!isLeaf()) {
            for(Label attributeValue: children.keySet()) {
                T child = children.get(attributeValue);
                child.depthFirstPreOrderVisit(v);
                v.visitEdge(this, child,attributeValue);
            }
        }
    }
    
    /** uses the visitor v to walk the tree in a depth-first post-order fashion, that is recursively
     * one-by-one all the sub trees are visited, and then the root of the tree is visited. The order of 
     * visiting is shown in the following image:
     * <br><img  src="doc-files/demotree-postorder.png">
     * 
     * @param v  visitor
     * @see Visitor
     */
    public void depthFirstPostOrderVisit(Visitor<Label,T> v) {
        // depth first
        if (!isLeaf()) {
            for(Label attributeValue: children.keySet()) {
                T child = children.get(attributeValue);
                child.depthFirstPostOrderVisit(v);
            }
        }
        
        // post order node
        v.visitNode(this);
        
        // post order edges
        if (!isLeaf()) {
            for(Label attributeValue: children.keySet()) {
                T child = children.get(attributeValue);
                v.visitEdge(this, child,attributeValue);
            }
        }
    }
    
    /** returns a dot representation of this tree. 
     * @return dot representation
     * @see <a href="http://sandbox.kidstrythisathome.com/erdos">View dot files online</a>
     * @see <a href="http://www.graphviz.org">Graphviz</a>
     */
    public String toDot() {
        final StringBuilder b = new StringBuilder("digraph DT {\n");
        Visitor<Label,T> visitor = new DotVisitor<>(b);                
        depthFirstPreOrderVisit(visitor);
        return b.append("}").toString();
    }
}