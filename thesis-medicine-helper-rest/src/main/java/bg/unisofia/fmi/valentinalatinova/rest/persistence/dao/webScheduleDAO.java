package bg.unisofia.fmi.valentinalatinova.rest.persistence.dao;

import bg.unisofia.fmi.valentinalatinova.rest.data.WebScheduleDO;
import bg.unisofia.fmi.valentinalatinova.rest.persistence.DataBaseCommander;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Valentina on 2/8/2016.
 */
public class WebScheduleDAO {

    private DataBaseCommander dataBaseCommander;

    public WebScheduleDAO(DataBaseCommander dataBaseCommander) {
        this.dataBaseCommander = dataBaseCommander;
    }

    public long addScheduleByUserId(final WebScheduleDO scheduleToAdd, final long userId) {
        String sql = "INSERT INTO `schedulesunited` (startDate, endDate, Description, frequencyValue, frequencyTypes, Patients_ID, Doctors_ID, Diagnoses_ID) VALUES(?,?,?,?,?,?,(Select `Doctor_ID` from `Users` where `ID`=?),?);";
        PreparedStatement pr = dataBaseCommander.createPreparedStatement(sql, scheduleToAdd.getStartDate(), scheduleToAdd.getEndDate(), scheduleToAdd.getDescription(), scheduleToAdd.getFrequencyValue(), scheduleToAdd.getFrequencyType(), scheduleToAdd.getPatientId() == 0 ? null : scheduleToAdd.getPatientId(), userId, scheduleToAdd.getDiagnoseId());
        return dataBaseCommander.insertWithReturnNewID(pr);
    }

    public List<WebScheduleDO> getScheduleByDiagnoseIdWithLimits(final int diagnoseId, final String start, final String end, final long userId) {
        String sql = "SELECT * FROM `schedulesunited` Where `Diagnoses_ID`=? AND `Doctors_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, diagnoseId, userId, end);
    }

    public List<WebScheduleDO> getScheduleByDiagnoseId(final int diagnoseId, final long userId) {
        String sql = "SELECT * FROM `schedulesunited` Where `Diagnoses_ID`=? AND `Doctors_ID`=(Select `Doctor_ID` from `Users` where `ID`=?)";
        return dataBaseCommander.select(WebScheduleDO.class, sql, diagnoseId, userId);
    }

    public void addScheduleToPatientFromDefaultDiagnose(final int diagnoseId, final String startDate, final String patientId, final long userId)
    {
        String sql = "insert into schedulesunited (startDate, endDate, Description, frequencyValue, frequencyTypes, Patients_ID, Doctors_ID, Diagnoses_ID) select ?, date_add(?, interval datediff(startDate, endDate) day), Description, frequencyValue, frequencyTypes, ?, Doctors_ID, null from schedulesunited where Diagnoses_ID=?";
        PreparedStatement pr = dataBaseCommander.createPreparedStatement(sql, startDate, startDate, patientId, diagnoseId);
         dataBaseCommander.insertWithReturnNewID(pr);
    }

}
