/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.labeledtree;

/**
 * This class shows how to create a LabeledTree. In this case a tree labeled with
 * doubles. The resulting tree is rendered using dot and shown in a webbrowser.
 * It should be something like this:
 * <br><img src="doc-files/demotree.png"/>
 * @author huub
 */
public class DemoTree extends LabeledTree<Double,DemoTree> {
    public static void main(String[] a) {
        DemoTree leaf1 = new DemoTree();
        DemoTree leaf2 = new DemoTree();
        DemoTree child1 = new DemoTree();
        child1.addSubTree(1.0, leaf1);
        child1.addSubTree(Double.NaN, leaf2);

        DemoTree leaf3 = new DemoTree();
        DemoTree child2 = new DemoTree();
        child2.addSubTree(3.0, leaf3);
        
        DemoTree root = new DemoTree();
        root.addSubTree(1.0, child1);
        root.addSubTree(2.0, child2);
        
        DotUtil.showDotInBrowser(root.toDot());
    }
}
