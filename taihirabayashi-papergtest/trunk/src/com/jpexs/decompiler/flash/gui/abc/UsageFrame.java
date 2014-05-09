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
package com.jpexs.decompiler.flash.gui.abc;

import com.jpexs.decompiler.flash.abc.ABC;
import com.jpexs.decompiler.flash.abc.usages.InsideClassMultinameUsage;
import com.jpexs.decompiler.flash.abc.usages.MethodMultinameUsage;
import com.jpexs.decompiler.flash.abc.usages.MultinameUsage;
import com.jpexs.decompiler.flash.abc.usages.TraitMultinameUsage;
import com.jpexs.decompiler.flash.gui.AppFrame;
import com.jpexs.decompiler.flash.gui.View;
import com.jpexs.decompiler.flash.tags.ABCContainerTag;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author JPEXS
 */
public class UsageFrame extends AppFrame implements ActionListener, MouseListener {

    static final String ACTION_GOTO = "GOTO";
    static final String ACTION_CANCEL = "CANCEL";

    private final JButton gotoButton = new JButton(translate("button.goto"));
    private final JButton cancelButton = new JButton(translate("button.cancel"));
    private final JList usageList;
    private final UsageListModel usageListModel;
    private final ABC abc;
    private final ABCPanel abcPanel;

    public UsageFrame(List<ABCContainerTag> abcTags, ABC abc, int multinameIndex, ABCPanel abcPanel) {
        this.abcPanel = abcPanel;
        List<MultinameUsage> usages = abc.findMultinameUsage(multinameIndex);
        this.abc = abc;
        usageListModel = new UsageListModel(abcTags, abc);
        for (MultinameUsage u : usages) {
            usageListModel.addElement(u);
        }
        usageList = new JList<>(usageListModel);
        usageList.setBackground(Color.white);
        gotoButton.setActionCommand(ACTION_GOTO);
        gotoButton.addActionListener(this);
        cancelButton.setActionCommand(ACTION_CANCEL);
        cancelButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(gotoButton);
        buttonsPanel.add(cancelButton);

        usageList.addMouseListener(this);
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        cont.add(new JScrollPane(usageList), BorderLayout.CENTER);
        cont.add(buttonsPanel, BorderLayout.SOUTH);
        setSize(400, 300);
        setTitle(translate("dialog.title") + abc.constants.getMultiname(multinameIndex).getNameWithNamespace(abc.constants));
        View.centerScreen(this);
        View.setWindowIcon(this);
    }

    private void gotoUsage() {
        if (usageList.getSelectedIndex() != -1) {
            MultinameUsage usage = usageListModel.getUsage(usageList.getSelectedIndex());
            if (usage instanceof InsideClassMultinameUsage) {
                InsideClassMultinameUsage icu = (InsideClassMultinameUsage) usage;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
                if (usage instanceof TraitMultinameUsage) {
                    TraitMultinameUsage tmu = (TraitMultinameUsage) usage;
                    int traitIndex;
                    if (tmu.parentTraitIndex > -1) {
                        traitIndex = tmu.parentTraitIndex;
                    } else {
                        traitIndex = tmu.traitIndex;
                    }
                    if (!tmu.isStatic) {
                        traitIndex += abc.class_info.get(tmu.classIndex).static_traits.traits.size();
                    }
                    if (tmu instanceof MethodMultinameUsage) {
                        MethodMultinameUsage mmu = (MethodMultinameUsage) usage;
                        if (mmu.isInitializer == true) {
                            traitIndex = abc.class_info.get(mmu.classIndex).static_traits.traits.size() + abc.instance_info.get(mmu.classIndex).instance_traits.traits.size() + (mmu.isStatic ? 1 : 0);
                        }
                    }
                    abcPanel.decompiledTextArea.gotoTrait(traitIndex);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ACTION_GOTO:
                gotoUsage();
                setVisible(false);
                break;
            case ACTION_CANCEL:
                setVisible(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            gotoUsage();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
