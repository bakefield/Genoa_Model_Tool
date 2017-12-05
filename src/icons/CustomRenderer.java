package icons;

import com.janusresearch.genoaModelTool.genoa.Entity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CustomRenderer implements TreeCellRenderer {
    private JLabel label;

    public CustomRenderer() {
        label = new JLabel();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof Entity) {
            Entity entity = (Entity) o;
            label.setIcon(GenoaIcons.Objects.Entity);
            label.setText(entity.getNodeText());
        }
        else {
            label.setIcon(GenoaIcons.Objects.GenoaModelFile_alt1);
        }
        return label;
    }
}