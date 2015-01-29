package com.alexterprogs.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DepartmentFiller extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea;
    private JLabel labelInfo;
    private JScrollPane scroller;

    public DepartmentFiller (JComboBox<String> departments) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Список отделов");

        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);

        labelInfo.setText("<html>Редактируйте поле ниже для добавления или удаления отдела.<br>Каждый отдел вводите в отдельную строчку.</html> ");
        labelInfo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("Icons/info.png")));
        textArea.setLineWrap(false);
        scroller.setPreferredSize(new Dimension(250, 250));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < departments.getItemCount(); i++) {
            sb.append(departments.getItemAt(i));
            sb.append("\n");
        }
        textArea.setText(sb.toString());

        /**/
        buttonOK.addActionListener(l -> {
            departments.removeAllItems();
            Set<String> set = new TreeSet<>();
            for (String str : textArea.getText().split("\\n")) {
                if (str.trim().equals("")) {
                    continue;
                }
                set.add(str.trim());
            }
            set.forEach(departments:: addItem);
            this.dispose();
        });


        buttonCancel.addActionListener(new ClosingWindowListener());
        this.addWindowListener(new ClosingWindowListener());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }


    private class ClosingWindowListener extends WindowAdapter implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent e) {
            onClose();
        }

        @Override
        public void windowClosing (WindowEvent e) {
            onClose();
        }

        private void onClose () {
            dispose();
        }
    }
}
