package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileTableDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.MobileTableValueDto;
import bg.unisofia.fmi.valentinalatinova.rest.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDaoo;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileTableDaoImpl implements MobileTableDaoo {
    private static List<MobileTableDto> tables = new ArrayList<>();

    static {
        List<MobileTableValueDto> values1 = new ArrayList<>();
        values1.add(new MobileTableValueDto(1, "Measurement1", DateTime.now(), 1));
        values1.add(new MobileTableValueDto(1, "Measurement2", DateTime.now(), 1));
        tables.add(new MobileTableDto(1, "Table1", values1, 1));

        List<MobileTableValueDto> values2 = new ArrayList<>();
        values2.add(new MobileTableValueDto(1, "Measurement1", DateTime.now(), 2));
        values2.add(new MobileTableValueDto(1, "Measurement2", DateTime.now(), 2));
        tables.add(new MobileTableDto(2, "Table2", values2, 1));
    }

    @Override
    public List<MobileTableDto> getTables(long userId) {
        return tables;
    }

    @Override
    public ResultDto save(MobileTableValueDto dto) {
        return null;
    }

    @Override
    public ResultDto update(MobileTableValueDto dto) {
        return null;
    }

    @Override
    public ResultDto delete(long id) {
        return null;
    }
}
