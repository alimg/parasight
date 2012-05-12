/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.reader.Reader;
import com.genoscope.renderer.GenoscopeRenderer;
import com.genoscope.renderer.visualizers.*;
import com.genoscope.types.Chromosome;
import com.genoscope.types.PairBlock;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class State {

	private Vector<Chromosome> chromosomeList;
	private Vector<PairBlock> pairBlockList;
	private Vector<Visualizer> visualizerList;
	private Vector<InterChromosomeV> pairingVisualizerList;
	private GenoscopeRenderer renderer;
	private DefaultMutableTreeNode chromosomeTree;
	private DefaultMutableTreeNode pairingTree;
	private JTree objectTree;

	public State() {
		chromosomeList = new <Chromosome>Vector();
		pairBlockList = new <PairBlock>Vector();
		visualizerList = new <Visualizer>Vector();
	}

	public void setChromosomeTree(DefaultMutableTreeNode model) {
		chromosomeTree = model;
	}

	public void setPairingTree(DefaultMutableTreeNode model) {
		pairingTree = model;
	}

	public void setObjectTree(JTree objectTree) {
		this.objectTree = objectTree;
	}

	public void setRenderer(GenoscopeRenderer renderer) {
		this.renderer = renderer;
		visualizerList = renderer.getVisualizerList();
                pairingVisualizerList = renderer.getPairingVisualizerList();
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

	public Vector<Chromosome> getChromosomeList() {
		return chromosomeList;
	}

	public Chromosome getChromosome(String path, String name) {
		for (Chromosome i : chromosomeList) {
			if (i.getSourceFile().equals(path) && i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	public Vector<PairBlock> getPairBlockList() {
		return pairBlockList;
	}

	public Visualizer getChromosomeVisualizer(String name, String path_) {
		for (Visualizer i : visualizerList) {
			if (i.getClass().equals(PairingVisualizer.class)) {
				continue;
			}
			String path = ((ChromosomeVisualizer) i).getChromosomePath();
			String fileName = null, fileName2 = null;
			if (path.lastIndexOf("/") == -1 && path.lastIndexOf("\\") == -1) {
				fileName = path;
			} else {
				fileName = path.substring(Math.max(path.lastIndexOf('/') + 1, path.lastIndexOf('\\')) + 1);
			}
			if (path_.lastIndexOf("/") == -1 && path_.lastIndexOf("\\") == -1) {
				fileName2 = path_;
			} else {
				fileName2 = path_.substring(Math.max(path_.lastIndexOf('/') + 1, path_.lastIndexOf('\\')) + 1);
			}
			if (((ChromosomeVisualizer) i).getChromosomeName().equals(name) && fileName.equals(fileName2)) {
				return i;
			}
		}
		return null;
	}

	public void addChromosome(Chromosome chr) {
		DefaultMutableTreeNode chromosomeNode = null;
		chromosomeList.add(chr);
		int lastInd = chr.getSourceFile().lastIndexOf('.');
		String extension = chr.getSourceFile().substring(lastInd + 1);
		String fileName = chr.getSourceFile().substring(Math.max(chr.getSourceFile().lastIndexOf('/')+1, chr.getSourceFile().lastIndexOf('\\')) + 1);
		for (Enumeration p = chromosomeTree.children(); p.hasMoreElements();) {
			DefaultMutableTreeNode chrNode = (DefaultMutableTreeNode) p.nextElement();
			if (chrNode.getUserObject().equals(chr.getName())) {
				chromosomeNode = chrNode;
				break;
			}
		}
		if (chromosomeNode == null) {
			chromosomeNode = new DefaultMutableTreeNode(chr.getName());
			chromosomeTree.add(chromosomeNode);
		}
		switch (extension) {
			case "bed":
				chromosomeNode.add(new DefaultMutableTreeNode(chr.getName() + " - " + fileName + " - BED data"));
				renderer.addVisualizer(new BEDVisualizer(800, 80, chr));
				break;
			case "cb":
				chromosomeNode.add(new DefaultMutableTreeNode(chr.getName() + " - " + fileName + " - Cytoband data"));
				renderer.addVisualizer(new CBVisualizer(800, 80, chr));
				break;
			case "rd":
				chromosomeNode.add(new DefaultMutableTreeNode(chr.getName() + " - " + fileName + " - ReadDepth data"));
				renderer.addVisualizer(new ReadDepthVisualizer(800, 80, chr));
				break;
			case "cn":
				break;
			case "bedpe":
				chromosomeNode.add(new DefaultMutableTreeNode(chr.getName() + " - " + fileName + " - BEDPE data"));
				renderer.addVisualizer(new BEDVisualizer(800, 80, chr));
				break;
			case "psa":
				chromosomeNode.add(new DefaultMutableTreeNode(chr.getName() + " - " + fileName + " - PSA data"));
				renderer.addVisualizer(new PSAVisualizer(800, 80, chr));
				break;
			default:
				renderer.addVisualizer(new ChromosomeVisualizer(800, 80, chr));
				break;
		}
	}

	public void addPairBlock(PairBlock pairBlock) {
		pairBlockList.add(pairBlock);
		ChromosomeVisualizer v1 = (ChromosomeVisualizer) getChromosomeVisualizer(pairBlock.getFirst().getName(), pairBlock.getFirst().getSourceFile());
		ChromosomeVisualizer v2 = (ChromosomeVisualizer) getChromosomeVisualizer(pairBlock.getSecond().getName(), pairBlock.getSecond().getSourceFile());
		renderer.addVisualizer(new PairingVisualizer(800, 80, v1, v2, pairBlock));

		DefaultMutableTreeNode pairingNode = null;
		String name = pairBlock.getFirst().getName() + " - "
				+ pairBlock.getSecond().getName() + " - " + pairBlock.getFirst().getSourceFile();

		pairingTree.add(new DefaultMutableTreeNode(name));
	}

	public Visualizer getPairingVisualizer(String string, String string0,String path) {
        for (Iterator<InterChromosomeV> it = pairingVisualizerList.iterator(); it.hasNext();) {
            Visualizer i = it.next();
            if (((PairingVisualizer) i).getPairs().getFirst().getName().equals(string)
                            && ((PairingVisualizer) i).getPairs().getSecond().getName().equals(string0)
                    && ((PairingVisualizer) i).getPairs().getFirst().getSourceFile().equals(path)) {
                    return i;
            }
        }
		return null;
	}

	public void showPairingVisualizer(String string,String path) {
            for (Iterator<InterChromosomeV> it = pairingVisualizerList.iterator(); it.hasNext();) {
                Visualizer i = it.next();
                PairingVisualizer k = ((PairingVisualizer) i);
                if(!k.arePairsVisible())
                    continue;
                if(!k.getPairs().getFirst().getName().equals(string))
                    if(!k.getPairs().getSecond().getName().equals(string))
                    continue;
                if(!k.getPairs().getFirst().getSourceFile().equals(path))
                    continue;
                k.setVisible(true);
            }
        }
        
	void reset() {
		chromosomeList.clear();
		visualizerList.clear();
		pairBlockList.clear();
	}

	public void clone(State state_) {
		for (Chromosome i : chromosomeList)
			state_.addChromosome(i);
		for (PairBlock i : pairBlockList)
			state_.addPairBlock(i);
	}

    public void toggleAll(boolean x) {
        for(Visualizer i:visualizerList)
            i.setVisible(x);
        if(x == false)
        for(InterChromosomeV i:pairingVisualizerList)
            i.setVisible(x);
    }

    public void showPairsOf(Visualizer v) {
        if(!(v instanceof ChromosomeVisualizer))
            return;
        showPairingVisualizer(((ChromosomeVisualizer)v).getChromosomeName(), ((ChromosomeVisualizer)v).getChromosomePath());
    }

}
