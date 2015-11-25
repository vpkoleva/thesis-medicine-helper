SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `medicinehelper` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `medicinehelper` ;

-- -----------------------------------------------------
-- Table `medicinehelper`.`Specializations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Specializations` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Doctors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Doctors` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Specialization_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Doctors_Specializations_idx` (`Specialization_ID` ASC),
  CONSTRAINT `fk_Doctor_specialization1`
    FOREIGN KEY (`Specialization_ID`)
    REFERENCES `medicinehelper`.`Specializations` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Diagnoses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Diagnoses` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Diagnose` VARCHAR(150) NULL,
  `Doctor_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Diagnoses_Doctors_idx` (`Doctor_ID` ASC),
  CONSTRAINT `fk_Diagnose_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Patients` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `Diagnose_ID` INT NOT NULL,
  `Doctor_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Patients_Diagnoses_idx` (`Diagnose_ID` ASC),
  INDEX `fk_Patients_Doctors_idx` (`Doctor_ID` ASC),
  CONSTRAINT `fk_Users_Diagnose1`
    FOREIGN KEY (`Diagnose_ID`)
    REFERENCES `medicinehelper`.`Diagnoses` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`DurationTypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`DurationTypes` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `durationValues` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Schedules`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Schedules` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `frequencyType` INT NOT NULL,
  `frequencyValue` INT NULL,
  `Action` VARCHAR(200) NULL,
  `durationType` INT NOT NULL,
  `durationValue` INT NULL,
  `Doctor_ID` INT NOT NULL,
  `isDefault` TINYINT(1) NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `idShedule_UNIQUE` (`ID` ASC),
  INDEX `fk_Shedules_DurationTypes2_idx` (`frequencyType` ASC),
  INDEX `fk_Shedules_DurationTypes1_idx` (`durationType` ASC),
  INDEX `fk_Schedules_Doctors_idx` (`Doctor_ID` ASC),
  CONSTRAINT `fk_Shedule_durationTypes2`
    FOREIGN KEY (`frequencyType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Shedule_durationTypes1`
    FOREIGN KEY (`durationType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Templates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Templates` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(200) NULL,
  `diagnose_ID` INT NOT NULL,
  `Doctor_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Templates_Diagnoses_idx` (`diagnose_ID` ASC),
  INDEX `fk_Templates_Doctors_idx` (`Doctor_ID` ASC),
  CONSTRAINT `fk_Templates_Diagnose1`
    FOREIGN KEY (`diagnose_ID`)
    REFERENCES `medicinehelper`.`Diagnoses` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Templates_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`TemplateToSchedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`TemplateToSchedule` (
  `Templates_ID` INT NOT NULL,
  `Schedule_ID` INT NOT NULL,
  `startAfter` DATE NULL,
  `startAfterType` INT NOT NULL,
  INDEX `fk_TemplateToShcedule_Templates1_idx` (`Templates_ID` ASC),
  INDEX `fk_TemplateToShcedule_Shedule1_idx` (`Schedule_ID` ASC),
  INDEX `fk_TemplateToShcedule_durationTypes1_idx` (`startAfterType` ASC),
  CONSTRAINT `fk_TemplateToShcedule_Templates1`
    FOREIGN KEY (`Templates_ID`)
    REFERENCES `medicinehelper`.`Templates` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TemplateToShcedule_Shedule1`
    FOREIGN KEY (`Schedule_ID`)
    REFERENCES `medicinehelper`.`Schedules` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TemplateToShcedule_durationTypes1`
    FOREIGN KEY (`startAfterType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`MobileUsers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`MobileUsers` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Doctor_ID` INT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_MobileUsers_Doctors_idx` (`Doctor_ID` ASC),
  CONSTRAINT `fk_mobileUser_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mUserDocuments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mUserDocuments` (
  `mobileUser_ID` INT NOT NULL,
  INDEX `fk_mUserDocuments_mobileUser1_idx` (`mobileUser_ID` ASC),
  CONSTRAINT `fk_mUserDocuments_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`DoctorsByUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`DoctorsByUser` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `workingHours` VARCHAR(100) NULL,
  `phone` VARCHAR(45) NULL,
  `latitude` FLOAT NULL,
  `longitute` FLOAT NULL,
  `mobileUser_ID` INT NOT NULL,
  `specialization_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_DoctorsByUser_mobileUser1_idx` (`mobileUser_ID` ASC),
  INDEX `fk_DoctorsByUser_specialization1_idx` (`specialization_ID` ASC),
  CONSTRAINT `fk_DoctorsByUser_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DoctorsByUser_specialization1`
    FOREIGN KEY (`specialization_ID`)
    REFERENCES `medicinehelper`.`Specializations` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Raitings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Raitings` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `raitingValue` DOUBLE NOT NULL,
  `Comment` VARCHAR(245) NULL,
  `DoctorsByUser_ID` INT NOT NULL,
  `mobileUser_ID` INT NOT NULL,
  `rateDate` DATETIME NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_Raitings_DoctorsByUser1_idx` (`DoctorsByUser_ID` ASC),
  INDEX `fk_Raitings_mobileUser1_idx` (`mobileUser_ID` ASC),
  CONSTRAINT `fk_Raitings_DoctorsByUser1`
    FOREIGN KEY (`DoctorsByUser_ID`)
    REFERENCES `medicinehelper`.`DoctorsByUser` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Raitings_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`UserToMobileUsers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`UserToMobileUsers` (
  `mobileUser_ID` INT NOT NULL,
  `Users_ID` INT NOT NULL,
  INDEX `fk_UserToMobileUsers_mobileUser1_idx` (`mobileUser_ID` ASC),
  INDEX `fk_UserToMobileUsers_Users1_idx` (`Users_ID` ASC),
  CONSTRAINT `fk_UserToMobileUsers_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserToMobileUsers_Users1`
    FOREIGN KEY (`Users_ID`)
    REFERENCES `medicinehelper`.`Patients` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`UserToShedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`UserToShedule` (
  `Users_ID` INT NOT NULL,
  `Schedule_ID` INT NOT NULL,
  `startAfter` DATE NULL,
  `startAfterType` INT NOT NULL,
  INDEX `fk_UserToShedule_Users1_idx` (`Users_ID` ASC),
  INDEX `fk_UserToShedule_Schedule1_idx` (`Schedule_ID` ASC),
  INDEX `fk_UserToShedule_durationTypes1_idx` (`startAfterType` ASC),
  CONSTRAINT `fk_UserToShedule_Users1`
    FOREIGN KEY (`Users_ID`)
    REFERENCES `medicinehelper`.`Patients` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserToShedule_Schedule1`
    FOREIGN KEY (`Schedule_ID`)
    REFERENCES `medicinehelper`.`Schedules` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserToShedule_durationTypes1`
    FOREIGN KEY (`startAfterType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mUserSchedules`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mUserSchedules` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(100) NULL,
  `startDate` DATETIME NULL,
  `duration` INT NULL,
  `mobileUser_ID` INT NOT NULL,
  `durationType` INT NOT NULL,
  `frequency` INT NULL,
  `frequencyType` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_mUserSchedules_mobileUser1_idx` (`mobileUser_ID` ASC),
  INDEX `fk_mUserSchedules_durationTypes1_idx` (`durationType` ASC),
  INDEX `fk_mUserSchedules_durationTypes2_idx` (`frequencyType` ASC),
  CONSTRAINT `fk_mUserSchedules_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mUserSchedules_durationTypes1`
    FOREIGN KEY (`durationType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mUserSchedules_durationTypes2`
    FOREIGN KEY (`frequencyType`)
    REFERENCES `medicinehelper`.`DurationTypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Roles` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Roles` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`Users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `passwordHash` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `Doctor_ID` INT NULL,
  `mobileUser_ID` INT NULL,
  `Roles_ID` INT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_RealUsers_Doctor1_idx` (`Doctor_ID` ASC),
  INDEX `fk_RealUsers_mobileUser1_idx` (`mobileUser_ID` ASC),
  INDEX `fk_RealUsers_Roles1_idx` (`Roles_ID` ASC),
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC),
  CONSTRAINT `fk_RealUsers_Doctor1`
    FOREIGN KEY (`Doctor_ID`)
    REFERENCES `medicinehelper`.`Doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RealUsers_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RealUsers_Roles1`
    FOREIGN KEY (`Roles_ID`)
    REFERENCES `medicinehelper`.`Roles` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mTables`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mTables` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `mobileUser_ID` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mTables_mobileUser1_idx` (`mobileUser_ID` ASC),
  CONSTRAINT `fk_mTables_mobileUser1`
    FOREIGN KEY (`mobileUser_ID`)
    REFERENCES `medicinehelper`.`MobileUsers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mTableValues`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mTableValues` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `measurement` VARCHAR(45) NOT NULL,
  `measurementDate` DATETIME NOT NULL,
  `mTables_id` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_mTableValues_mTables1_idx` (`mTables_id` ASC),
  CONSTRAINT `fk_mTableValues_mTables1`
    FOREIGN KEY (`mTables_id`)
    REFERENCES `medicinehelper`.`mTables` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `medicinehelper`.`Users`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`Users` (`ID`, `userName`, `passwordHash`, `firstName`, `lastName`, `Doctor_ID`, `mobileUser_ID`, `Roles_ID`) VALUES (1, 'na', 'na', 'NoAuth', 'NoAuth', NULL, NULL, NULL);

COMMIT;

