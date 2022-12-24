package com.github.kettoleon.ti.dashboard.view.components;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.model.alert.Alert;
import com.github.kettoleon.ti.dashboard.model.tables.*;
import com.github.kettoleon.ti.dashboard.view.util.DarculaColorPalette;
import com.github.kettoleon.ti.meta.MetaRepository;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainDashboard {
    private JPanel panel;
    private JTable availableCouncilorTable;
    private JTable hiredCouncilorTable;
    private JTabbedPane tabbedPane1;
    private JTable alertsTable;
    private JTable obtainableOrganisationsTable;
    private JTabbedPane tabbedPane2;
    private JLabel hate;
    private DashboardModel model;
    private MetaRepository meta;

    public MainDashboard(DashboardModel model, MetaRepository meta) {

        this.model = model;
        this.meta = meta;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        availableCouncilorTable = createCouncilorTable(model.getAvailableCouncilors());
        hiredCouncilorTable = createCouncilorTable(model.getHiredCouncilors());
        alertsTable = createResizableTable(model.getAlerts());
        alertsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new AlertPanelBuilder().getPanel((Alert) value);
            }
        });

        alertsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (alertsTable.getSelectedRow() >= 0) {
                    model.getAlerts().hideAlert((Alert) alertsTable.getValueAt(alertsTable.getSelectedRow(), 0));
                }
            }
        });

        obtainableOrganisationsTable = createOrganisationsTable(model.getObtainableOrganisations());
        hate = new JLabel("Alien hate: " + model.getHate());
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("hate")) {
                    double oldHate = (double) evt.getOldValue();
                    double newHate = (double) evt.getNewValue();
                    hate.setText("<html>Alien hate: " + model.getHate() + "</html>");
                    if (newHate > oldHate) {
                        hate.setIcon(FontIcon.of(BootstrapIcons.ARROW_UP_RIGHT_SQUARE_FILL, DarculaColorPalette.RED_NOTIFICATION));
                    } else if (newHate < oldHate) {
                        hate.setIcon(FontIcon.of(BootstrapIcons.ARROW_DOWN_RIGHT_SQUARE_FILL, DarculaColorPalette.GREEN_NOTIFICATION));
                    } else {
                        hate.setIcon(FontIcon.of(FontAwesomeSolid.EQUALS, DarculaColorPalette.BLUE_NOTIFICATION));
                    }
                    hate.repaint();
                }
            }
        });

    }

    private JTable createOrganisationsTable(OrganisationTableModel availableOrganisations) {
        JTable table = createResizableTable(availableOrganisations);
        table.getTableHeader().setDefaultRenderer(new EnhancedTableHeaderCellRenderer(OrganisationTableColumn.values()));
        table.setDefaultRenderer(Object.class, new OrganisationTableCellRenderer());
        table.setDefaultRenderer(Integer.class, new OrganisationTableCellRenderer());
        table.setDefaultRenderer(Double.class, new OrganisationTableCellRenderer());
        return table;
    }


    private JTable createCouncilorTable(CouncilorTableModel availableCouncilors) {
        JTable table = createResizableTable(availableCouncilors);
        table.setDefaultRenderer(Object.class, new CouncilorTableCellRenderer(meta));
        table.setDefaultRenderer(Integer.class, new CouncilorTableCellRenderer(meta));
        table.getTableHeader().setDefaultRenderer(new EnhancedTableHeaderCellRenderer(CouncilorTableColumn.values()));
        return table;
    }

    private static JTable createResizableTable(TableModel model) {
        JTable table = new JTable(model) {

            @Override
            public void resize(int width, int height) {
                resizeColumnWidth(this);
                super.resize(width, height);
            }

            @Override
            public void repaint(Rectangle r) {
                resizeColumnWidth(this);
                super.repaint(r);
            }

            public void resizeColumnWidth(JTable table) {
                final TableColumnModel columnModel = table.getColumnModel();
                for (int column = 0; column < table.getColumnCount(); column++) {
                    int width = 15; // Min width
                    for (int row = 0; row < table.getRowCount(); row++) {
                        TableCellRenderer renderer = table.getCellRenderer(row, column);
                        Component comp = table.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 1, width);
                        if (comp instanceof JLabel) {
                            JLabel jLabel = (JLabel) comp;
                            int height = jLabel.getFontMetrics(jLabel.getFont()).getHeight();
                            table.setRowHeight(row, height + 4);
                        } else if (comp instanceof JPanel) {
                            table.setRowHeight(row, comp.getPreferredSize().height + 4);
                        }
                    }
                    columnModel.getColumn(column).setPreferredWidth(width);
                }
            }
        };
        return table;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public Container getPanel() {
        return panel;
    }

    private static class EnhancedTableHeaderCellRenderer extends DefaultTableCellRenderer {
        private Column[] columns;

        public EnhancedTableHeaderCellRenderer(Column[] columns) {

            this.columns = columns;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Column c = columns[column];
            JLabel headerComponent = new JLabel(c.getHeader());
            if (c.getIcon() != null) {
                int size = headerComponent.getFontMetrics(headerComponent.getFont()).getHeight();
                headerComponent.setIcon(new ImageIcon(c.getIcon().getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
                headerComponent.setText(null);
            }

            return headerComponent;
        }
    }
}
