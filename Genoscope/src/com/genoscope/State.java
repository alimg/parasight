/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.reader.Reader;
import com.genoscope.renderer.GenoscopeRenderer;
import com.genoscope.renderer.visualizers.*;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Pair;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class State {

	private Vector<Chromosome> chromosomeList;
	private Vector<Pair> pairList;
	private Vector<Visualizer> visualizerList;
	private GenoscopeRenderer renderer;
        private DefaultMutableTreeNode chromosomeTree;
        private DefaultMutableTreeNode pairingTree;
        private JTree objectTree;

	public State() {
		chromosomeList = new <Chromosome>Vector();
		pairList = new <Pair>Vector();
		visualizerList = new <Visualizer>Vector();
	}
        
        public void setChromosomeTree(DefaultMutableTreeNode model){
            chromosomeTree = model;
        }
        
        public void setPairingTree(DefaultMutableTreeNode model){
            pairingTree = model;
        }
        public void setObjectTree(JTree objectTree){
            this.objectTree = objectTree;
        }
        
	public void setRenderer(GenoscopeRenderer renderer) {
		this.renderer = renderer;
	}

	public int importData(String fileName) {
		int returnID = Reader.readFile(fileName, this);
		return returnID;
	}

	public boolean checkChromosome(String path) {
		for (Chromosome i : chromosomeList) {
			if (i.getSourceFile().equals(path)) {
				return true;
			}
		}
		return false;
	}

	public void addChromosome(Chromosome chr) {
                DefaultMutableTreeNode chromosomeNode = null;
		chromosomeList.add(chr);
		int lastInd = chr.getSourceFile().lastIndexOf('.');
		String extension = chr.getSourceFile().substring(lastInd + 1);
                for(Enumeration p = chromosomeTree.children();p.hasMoreElements();){
                    DefaultMutableTreeNode chrNode = (DefaultMutableTreeNode) p.nextElement();
                    if(chrNode.getUserObject().equals(chr.getName())){
                        chromosomeNode = chrNode;
                        break;
                    }
                }
                if(chromosomeNode == null){
                    chromosomeNode = new DefaultMutableTreeNode(chr.getName());
                    chromosomeTree.add(chromosomeNode);
                }
		switch (extension) {
			case "bed":
				renderer.addVisualizer(new BEDVisualizer(800, 80, chr));
				break;
			case "cb":
				renderer.addVisualizer(new CBVisualizer(800, 80, chr));
				break;
            case "rd":
				renderer.addVisualizer(new ReadDepthVisualizer(800, 80, chr));
				break;
			case "cn":
				break;
			default:
				renderer.addVisualizer(new ChromosomeVisualizer(800, 80, chr));
				break;
		}
	}

	public void addPair(Pair pair) {
		pairList.add(pair);
	}
}
