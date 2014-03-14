package nl.tue.s2id90.classification.labeledtree;

/**
 * Implement this interface for traveling a LabeledTree.
 * @author huub
 * @param <Label>
 * @param <T>
 * @see LabeledTree
 * @see LabeledTree#depthFirstPostOrderVisit(Visitor) 
 * @see LabeledTree#depthFirstPreOrderVisit(Visitor) 
 */
public interface Visitor<Label,T extends LabeledTree<Label,T>> {
    void visitNode(LabeledTree<Label,T> node);
    void visitEdge(LabeledTree<Label,T> parent, T child, Label label);
}
