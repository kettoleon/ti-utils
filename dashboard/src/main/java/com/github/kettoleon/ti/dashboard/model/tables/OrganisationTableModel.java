package com.github.kettoleon.ti.dashboard.model.tables;

import com.github.kettoleon.ti.saves.Organisation;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrganisationTableModel extends AbstractTableModel {

    private List<Organisation> organisation = new ArrayList<>();


    @Override
    public String getColumnName(int column) {
        return OrganisationTableColumn.values()[column].getHeader();
    }

    @Override
    public int getRowCount() {
        return organisation.size();
    }

    @Override
    public int getColumnCount() {
        return OrganisationTableColumn.values().length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return OrganisationTableColumn.values()[columnIndex].getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Organisation c = organisation.get(rowIndex);
        Function<Organisation, Object> councilorObjectFunction = OrganisationTableColumn.values()[columnIndex].getGetter();
        if (councilorObjectFunction == null || c == null) {
            if (getColumnClass(columnIndex).equals(Integer.class)) {
                return 0;
            }
            return "TBD";
        } else {
            return councilorObjectFunction.apply(c);
        }
    }

    public void setOrganisations(List<Organisation> organisation) {
        this.organisation = organisation;
        fireTableDataChanged();
    }

    public List<Organisation> getOrganisations() {
        return organisation;
    }
}
