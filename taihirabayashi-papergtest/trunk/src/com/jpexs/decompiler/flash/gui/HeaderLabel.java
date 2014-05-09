/*
 *  Copyright (C) 2010-2014 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.GeneralPath;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.JLabel;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;

/**
 *
 * @author JPEXS
 */
public class HeaderLabel extends JLabel {

    public HeaderLabel(String text) {
        super(text);
        //setBorder(BorderFactory.createRaisedBevelBorder());


        /*setBorder(new Border() {

         @Override
         public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
         g.setColor(Color.gray);
         g.drawLine(0, 0,width-1, 0);
         g.drawLine(0, 0, 0, height-1);
         g.setColor(Color.darkGray);
         g.drawLine(width-1, 0, width-1, height-1);
         g.drawLine(0, height-1, width-1, height-1);
         }

         @Override
         public Insets getBorderInsets(Component c) {
         return new Insets(2, 2, 2, 2);
         }

         @Override
         public boolean isBorderOpaque() {
         return false;
         }
         });*/
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(217, 232, 251));
        g.fillRect(0, 0, getWidth(), getHeight());
        StandardBorderPainter borderPainter = new StandardBorderPainter();

        Set<SubstanceConstants.Side> straightSides = EnumSet.of(SubstanceConstants.Side.BOTTOM);
        int dy = 0;
        float cornerRadius = 5f;
        int borderThickness = 1;
        int borderInsets = 0;
        GeneralPath contourInner = borderPainter.isPaintingInnerContour() ? SubstanceOutlineUtilities.getBaseOutline(getWidth(), getHeight() + dy,
                cornerRadius - borderThickness, straightSides, borderThickness + borderInsets)
                : null;

        GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(getWidth(),
                getHeight() + dy, cornerRadius, straightSides, borderInsets);
        borderPainter.paintBorder(g, this, getWidth(), getHeight() + dy,
                contour, contourInner, new OfficeBlue2007Skin().getActiveColorScheme(DecorationAreaType.HEADER));
        g.setColor(Color.black);
        JLabel lab = new JLabel(getText(), JLabel.CENTER);
        lab.setSize(getSize());
        lab.paint(g);
        //g.drawString(getText(), getWidth()/2-getFontMetrics(getFont()).stringWidth(getText())/2, getFont().getSize());
    }
}
