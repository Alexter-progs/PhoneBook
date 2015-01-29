package com.alexterprogs;

import com.alexterprogs.GUI.GUI;

import javax.swing.*;

public class Main {
    public static void main (String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
             /* IGNORE */
        }
        SwingUtilities.invokeLater(GUI::new);
    }
}
