package com.github.kettoleon.ti.dashboard.model.tables;

import com.github.kettoleon.ti.saves.Councilor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CouncilorTableModel extends AbstractTableModel {

    private List<Councilor> councilors = new ArrayList<>();


    @Override
    public String getColumnName(int column) {
        return CouncilorTableColumn.values()[column].getHeader();
    }

    @Override
    public int getRowCount() {
        return councilors.size();
    }

    @Override
    public int getColumnCount() {
        return CouncilorTableColumn.values().length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CouncilorTableColumn.values()[columnIndex].getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Councilor c = councilors.get(rowIndex);
        Function<Councilor, Object> councilorObjectFunction = CouncilorTableColumn.values()[columnIndex].getGetter();
        if (councilorObjectFunction == null || c == null) {
            if (getColumnClass(columnIndex).equals(Integer.class)) {
                return 0;
            }
            return "TBD";
        } else {
            return councilorObjectFunction.apply(c);
        }
    }

    public void setCouncilors(List<Councilor> councilors) {
        this.councilors = councilors;
        fireTableDataChanged();
    }

    public List<Councilor> getCouncilors() {
        return councilors;
    }

    public Optional<Councilor> getCouncilor(int councilorId) {
        for(Councilor c : councilors){
            if(c.getId() == councilorId){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
