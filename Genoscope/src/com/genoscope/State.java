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
                visualizerList = renderer.getVisualizerList();
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
        
        public Visualizer getChromosomeVisualizer(String name){
		for (Visualizer i : visualizerList)
			if (((ChromosomeVisualizer)i).getChromosomeName().equals(name))
				return i;
                return null;
        }

	public void addChromosome(Chromosome chr) {
                DefaultMutableTreeNode chromosomeNode = null;
		chromosomeList.add(chr);
		int lastInd = chr.getSourceFile().lastIndexOf('.');
		String extension = chr.getSourceFile().substring(lastInd + 1);
                String fileName = chr.getSourceFile().substring(Math.max(chr.getSourceFile().lastIndexOf('/'),chr.getSourceFile().lastIndexOf('\\'))+1);
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
                    System.out.println("New chromosome " + chr.getName() + " added to tree");
                }
		switch (extension) {
			case "bed":
                                chromosomeNode.add(new DefaultMutableTreeNode("BED data (" + fileName + ")"));
				renderer.addVisualizer(new BEDVisualizer(800, 80, chr));
				break;
			case "cb":
                                chromosomeNode.add(new DefaultMutableTreeNode("CytoBand data (" + fileName + ")"));
				renderer.addVisualizer(new CBVisualizer(800, 80, chr));
				break;
                        case "rd":
                                chromosomeNode.add(new DefaultMutableTreeNode("ReadDepth data (" + fileName + ")"));
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
