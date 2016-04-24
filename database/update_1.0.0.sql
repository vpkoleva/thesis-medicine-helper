SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `medicinehelper`.`UserToMobileUsers` 
RENAME TO  `medicinehelper`.`PatientToMobileUsers` ;

ALTER TABLE `medicinehelper`.`UserToShedule` 
RENAME TO  `medicinehelper`.`PatientToShedule` ;

ALTER TABLE `medicinehelper`.`Roles` 
DROP COLUMN `roleName`,
ADD COLUMN `Role Name` VARCHAR(45) NULL DEFAULT NULL AFTER `ID`;

CREATE TABLE IF NOT EXISTS `medicinehelper`.`SchedulesUnited` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `Description` VARCHAR(45) NOT NULL,
  `startDate` DATETIME NULL DEFAULT NULL,
  `endDate` DATETIME NULL DEFAULT NULL,
  `frequency` DOUBLE NULL DEFAULT NULL,
  `Patients_ID` INT(11) NOT NULL,
  `Doctors_ID` INT(11) NOT NULL,
  `Diagnoses_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_SchedulesUnited_Patients1_idx` (`Patients_ID` ASC),
  INDEX `fk_SchedulesUnited_Doctors1_idx` (`Doctors_ID` ASC),
  INDEX `fk_SchedulesUnited_Diagnoses1_idx` (`Diagnoses_ID` ASC),
  CONSTRAINT `fk_SchedulesUnited_Patients1`
    FOREIGN KEY (`Patients_ID`)
    REFERENCES `medicinehelper`.`Patients` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SchedulesUnited_Doctors1`
    FOREIGN KEY (`Doctors_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SchedulesUnited_Diagnoses1`
    FOREIGN KEY (`Diagnoses_ID`)
    REFERENCES `medicinehelper`.`Diagnoses` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
