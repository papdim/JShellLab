package jShellLabSci.math.plot.plotObjects;

import jShellLabSci.math.plot.render.AbstractDrawer;


public interface Editable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void edit(Object editParent);
    public void editnote(AbstractDrawer draw);
}
