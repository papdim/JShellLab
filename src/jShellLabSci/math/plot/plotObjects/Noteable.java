package jShellLabSci.math.plot.plotObjects;

import jShellLabSci.math.plot.render.AbstractDrawer;


public interface Noteable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void note(AbstractDrawer draw);
}