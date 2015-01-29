package com.alexterprogs.GUI;

import com.alexterprogs.PDF.BookPrinter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;

public class GUI extends JFrame {
    private JFrame frame = this;
    private JTable tblPersons;

    private JButton buttonDelete;
    private JButton buttonAdd;
    private JButton buttonUpdate;

    private JComboBox<String> departments;

    private JTextField tfFullName;
    private JTextField tfPhoneNumber;
    private JTextField tfJobPosition;
    private JTextField tfRoomNumber;

    private JPanel contentPane;

    public GUI () {
        initGUI();
    }

    private void initGUI () {
        setContentPane(contentPane);
        setTitle("Telephone book");

        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Icons/logo.png")).getImage());

        /* Menu Bar */
        JMenuBar menuBar = new JMenuBar();
        /* Menu File */
        {
            JMenu fileMenu = new JMenu("Файл");

            /* Items */
            /*New and Open - later*/
            //JMenuItem newItem = new JMenuItem("Новый...");
            //JMenuItem openItem = new JMenuItem("Открыть...");
            JMenuItem saveItem = new JMenuItem("Сохранить...");
            JMenuItem printItem = new JMenuItem("Печать");
            JMenuItem closeItem = new JMenuItem("Закрыть");

            /* Listeners */
            //newItem.addActionListener(new NewProjectListener());
            //openItem.addActionListener(new OpenProjectListener());
            saveItem.addActionListener(new SaveProjectListener());
            printItem.addActionListener(new PrintBookListener());
            closeItem.addActionListener(l -> dispose());

            //fileMenu.add(newItem);
            //fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(printItem);
            fileMenu.addSeparator();
            fileMenu.add(closeItem);

            menuBar.add(fileMenu);
        }

        /* Menu Settings */
        {
            JMenu settingsMenu = new JMenu("Настройки");

            /* Menu Styles */
            JMenu stylesItem = new JMenu("Стили");
            {
                /* Items + actionListeners + addListener */
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    JMenuItem item = new JMenuItem(info.getName());
                    item.addActionListener(new LookAndFeelChangeListener(info.getClassName()));
                    stylesItem.add(item);
                }
            }

            /* Menu item */
            JMenuItem departmentFillerItem = new JMenuItem("Список отделов");
            departmentFillerItem.addActionListener(new DepartmentFillerListener());


            /* Composing menu */
            settingsMenu.add(departmentFillerItem);
            settingsMenu.add(stylesItem);
            menuBar.add(settingsMenu);
        }

        /* Menu Help: will do later */
        /*
        {
            JMenu helpMenu = new JMenu("Помощь");

            JMenuItem aboutItem = new JMenuItem("О программе");

            aboutItem.addActionListener(new ShowAboutListener());

            helpMenu.add(aboutItem);

            menuBar.add(helpMenu);
        }
        */
        setJMenuBar(menuBar);

        //departments.addItem("Добавьте отделы через меню настройки");
        departments.setToolTipText("Для редактирования списка отделов зайдите в меню Настройки -> Список отделов");


        /* Table */
        tblPersons.setModel(new DefaultTableModel(new Object[][] {}, new Object[] {"Должность", "ФИО", "Телефон", "№ комнаты", "Отдел"}) {
            @Override
            public boolean isCellEditable (int rowIndex, int columnIndex) {
                return columnIndex != 4;
            }
        });

        tblPersons.setFillsViewportHeight(true);
        tblPersons.addMouseListener(new RowSelectMouseAdapter());

        /* Buttons */
        buttonAdd.addActionListener(new AddButtonListener());
        buttonDelete.addActionListener(new DeleteButtonListener());
        buttonUpdate.addActionListener(new UpdateButtonListener());

        /* Frame settings */
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void showErrorMessage (String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private class NewProjectListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            tfJobPosition.setText("");
            tfRoomNumber.setText("");
            tfFullName.setText("");
            tfPhoneNumber.setText("");

            DefaultTableModel tbModel = (DefaultTableModel) tblPersons.getModel();
            for (int row = tbModel.getRowCount() - 1; row >= 0; row--) {
                tbModel.removeRow(row);
            }
        }
    }

    private class OpenProjectListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            /* Implement opening file */
            }
        }
    }


    private class SaveProjectListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:/"));
            fileChooser.setDialogTitle("Сохранить");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Adobe acrobat document (*.pdf)", "pdf");
            fileChooser.setFileFilter(filter);

            fileChooser.setAcceptAllFileFilterUsed(false);

            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String file = fileChooser.getSelectedFile().getName();
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                if (file.matches("[^\\\\/:*?\"<>|]+\\.pdf")) {
                    new BookPrinter().save(filePath, (DefaultTableModel)tblPersons.getModel());
                } else if (file.matches("[^\\\\/:*?\"<>|]+")){
                    new BookPrinter().save(filePath + ".pdf", (DefaultTableModel)tblPersons.getModel());
                } else {
                    showErrorMessage("Недопустимое имя файла");
                }
            }
        }
    }

    private class PrintBookListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            new BookPrinter().print((DefaultTableModel)tblPersons.getModel());
        }
    }

    private class LookAndFeelChangeListener implements ActionListener {
        private String lookAndFeel;

        public LookAndFeelChangeListener (String lookAndFeel) {
            this.lookAndFeel = lookAndFeel;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                SwingUtilities.updateComponentTreeUI(frame);
                frame.pack();
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e1) {
                JOptionPane.showMessageDialog(frame, "Невозможно применить стиль. Попробуйте другой.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private class ShowAboutListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {

        }
    }

    private class RowSelectMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked (MouseEvent e) {
            if (! (tblPersons.getSelectedRow() == - 1 || tblPersons.getRowCount() == 0)) {
                DefaultTableModel model = (DefaultTableModel) tblPersons.getModel();
                tfJobPosition.setText(model.getValueAt(tblPersons.getSelectedRow(), 0).toString());
                tfFullName.setText(model.getValueAt(tblPersons.getSelectedRow(), 1).toString());
                tfPhoneNumber.setText(model.getValueAt(tblPersons.getSelectedRow(), 2).toString());
                tfRoomNumber.setText(model.getValueAt(tblPersons.getSelectedRow(), 3).toString());
                departments.setSelectedItem(model.getValueAt(tblPersons.getSelectedRow(), 4).toString());
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            DefaultTableModel model = (DefaultTableModel) tblPersons.getModel();

            if (! (tfFullName.getText().trim().equals("") || tfPhoneNumber.getText().trim().equals("") || tfRoomNumber.getText().trim().equals(""))) {
                model.addRow(new Object[] {tfJobPosition.getText(), tfFullName.getText(), tfPhoneNumber.getText(), tfRoomNumber.getText(), departments.getSelectedItem().toString()});
                int nextRow = tblPersons.getSelectedRow() + 1;
                tblPersons.setRowSelectionInterval(nextRow, nextRow);
            } else {
                showErrorMessage("Заполните все поля");
            }
        }
    }


    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            if (tblPersons.getSelectedRow() == - 1) {
                if (tblPersons.getRowCount() == 0) {
                    showErrorMessage("Таблица пуста");
                } else {
                    showErrorMessage("Выберите строку");
                }
            } else {
                DefaultTableModel model = (DefaultTableModel) tblPersons.getModel();

                int row = tblPersons.getSelectedRow();
                int newRow = row - 1;

                model.removeRow(row);

                if (newRow >= 0) {
                    tblPersons.setRowSelectionInterval(newRow, newRow);
                } else if (row == 0 && tblPersons.getRowCount() > 0) {
                    tblPersons.setRowSelectionInterval(0, 0);
                }
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            if (tblPersons.getSelectedRow() == - 1) {
                if (tblPersons.getRowCount() == 0) {
                    showErrorMessage("Таблица пуста");
                } else {
                    showErrorMessage("Выберите строку");
                }
            } else {
                DefaultTableModel model = (DefaultTableModel) tblPersons.getModel();
                model.setValueAt(tfJobPosition.getText(), tblPersons.getSelectedRow(), 0);
                model.setValueAt(tfFullName.getText(), tblPersons.getSelectedRow(), 1);
                model.setValueAt(tfPhoneNumber.getText(), tblPersons.getSelectedRow(), 2);
                model.setValueAt(tfPhoneNumber.getText(), tblPersons.getSelectedRow(), 3);
                model.setValueAt(departments.getSelectedItem().toString(), tblPersons.getSelectedRow(), 4);
            }
        }
    }

    private class DepartmentFillerListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent e) {
            new DepartmentFiller(departments);
        }
    }
}