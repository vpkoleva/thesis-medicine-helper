package bg.unisofia.fmi.valentinalatinova.rest.persistence.impl;

import bg.unisofia.fmi.valentinalatinova.core.dto.MobileTableDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.MobileTableValueDto;
import bg.unisofia.fmi.valentinalatinova.core.dto.ResultDto;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.MobileTableDao;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MobileTableDaoImpl implements MobileTableDao {
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
    public ResultDto save(MobileTableValueDto tableValue) {
        try {
            tables.get(0).getValues().add(tableValue);
            return ResultDto.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }

    @Override
    public ResultDto update(MobileTableValueDto tableValue) {
        try {
            tables.get(0).getValues().remove(tableValue);
            tables.get(0).getValues().add(tableValue);
            return ResultDto.createSuccess(tableValue.getId());
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }

    @Override
    public ResultDto delete(long id, long userId) {
        try {
            for (MobileTableValueDto tableValue : tables.get(0).getValues()) {
                if (tableValue.getId() == id) {
                    tables.get(0).getValues().remove(tableValue);
                    return ResultDto.createSuccess(id);
                }
            }
            return ResultDto.createError("Record with id=" + id + " not found");
        } catch (Exception ex) {
            return ResultDto.createError(ex.getMessage());
        }
    }
}
