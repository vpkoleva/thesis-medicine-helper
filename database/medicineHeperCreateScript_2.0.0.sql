SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `medicinehelper` DEFAULT CHARACTER SET utf8 ;
USE `medicinehelper` ;

-- -----------------------------------------------------
-- Table `medicinehelper`.`specializations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`specializations` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`doctors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`doctors` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `specializations_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_doctors_specializations_idx` (`specializations_ID` ASC),
  CONSTRAINT `fk_doctors_specializations`
    FOREIGN KEY (`specializations_ID`)
    REFERENCES `medicinehelper`.`specializations` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`diagnoses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`diagnoses` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `diagnose` VARCHAR(150) NULL DEFAULT NULL,
  `doctor_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_diagnoses_doctors_idx` (`doctor_ID` ASC),
  CONSTRAINT `fk_diagnose_doctor`
    FOREIGN KEY (`doctor_ID`)
    REFERENCES `medicinehelper`.`doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mobileusers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mobileusers` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`doctorsbyuser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`doctorsbyuser` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(45) NULL DEFAULT NULL,
  `workingHours` VARCHAR(100) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NULL,
  `latitude` FLOAT NULL DEFAULT NULL,
  `longitute` FLOAT NULL DEFAULT NULL,
  `mobileuser_ID` INT NOT NULL,
  `specialization_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_doctorsbyuser_mobileuser_idx` (`mobileuser_ID` ASC),
  INDEX `fk_doctorsbyuser_specialization_idx` (`specialization_ID` ASC),
  CONSTRAINT `fk_doctorsbyuser_mobileuser`
    FOREIGN KEY (`mobileuser_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_doctorsbyuser_specialization`
    FOREIGN KEY (`specialization_ID`)
    REFERENCES `medicinehelper`.`specializations` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`durationtypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`durationtypes` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mtables`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mtables` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `mobileuser_ID` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mtables_mobileuser_idx` (`mobileuser_ID` ASC),
  CONSTRAINT `fk_mtables_mobileuser`
    FOREIGN KEY (`mobileuser_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`mtablevalues`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`mtablevalues` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `measurement` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  `mtables_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_mtablevalues_mtables_idx` (`mtables_ID` ASC),
  CONSTRAINT `fk_mtablevalues_mtables`
    FOREIGN KEY (`mtables_ID`)
    REFERENCES `medicinehelper`.`mtables` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`muserdocuments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`muserdocuments` (
  `mobileuser_ID` INT NOT NULL,
  INDEX `fk_muserdocuments_mobileusers_idx` (`mobileuser_ID` ASC),
  CONSTRAINT `fk_mUserDocuments_mobileUser1`
    FOREIGN KEY (`mobileuser_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`patients` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(45) NULL DEFAULT NULL,
  `diagnose_ID` INT(11) NOT NULL,
  `doctor_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_patients_diagnoses_idx` (`diagnose_ID` ASC),
  INDEX `fk_patients_doctors_idx` (`doctor_ID` ASC),
  CONSTRAINT `fk_patients_diagnose`
    FOREIGN KEY (`diagnose_ID`)
    REFERENCES `medicinehelper`.`diagnoses` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_doctor`
    FOREIGN KEY (`doctor_ID`)
    REFERENCES `medicinehelper`.`doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`raitings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`raitings` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `raitingValue` DOUBLE NOT NULL,
  `Comment` VARCHAR(245) NULL,
  `doctorsbyuser_ID` INT NOT NULL,
  `mobileuser_ID` INT NOT NULL,
  `rateDate` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_raitings_doctorsbyuser_idx` (`doctorsbyuser_ID` ASC),
  INDEX `fk_raitings_mobileuser_idx` (`mobileuser_ID` ASC),
  CONSTRAINT `fk_Raitings_DoctorsByUser1`
    FOREIGN KEY (`doctorsbyuser_ID`)
    REFERENCES `medicinehelper`.`doctorsbyuser` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Raitings_mobileUser1`
    FOREIGN KEY (`mobileuser_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`schedules`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`schedules` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  `startDate` DATETIME NULL DEFAULT NULL,
  `endDate` DATETIME NULL DEFAULT NULL,
  `startAfterValue` INT NULL,
  `startAfterType` INT NULL,
  `frequencyValue` INT NULL,
  `frequencyTypes` INT NOT NULL,
  `endAfterValue` INT NULL,
  `endAfterType` INT NULL,
  `patients_ID` INT NULL,
  `doctors_ID` INT NULL,
  `mobileusers_ID` INT NULL,
  `diagnoses_ID` INT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  INDEX `fk_schedules_patients_idx` (`patients_ID` ASC),
  INDEX `fk_schedules_doctors_idx` (`doctors_ID` ASC),
  INDEX `fk_schedules_diagnoses_idx` (`diagnoses_ID` ASC),
  INDEX `fk_schedules_frequencyTypes_idx` (`frequencyTypes` ASC),
  INDEX `fk_schedules_endAfterType_idx` (`endAfterType` ASC),
  INDEX `fk_schedules_startAfterType_idx` (`startAfterType` ASC),
  INDEX `fk_schedules_mobileusers1_idx` (`mobileusers_ID` ASC),
  CONSTRAINT `fk_schedules_diagnoses`
    FOREIGN KEY (`diagnoses_ID`)
    REFERENCES `medicinehelper`.`diagnoses` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_fk_schedules_doctors`
    FOREIGN KEY (`doctors_ID`)
    REFERENCES `medicinehelper`.`doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_frequencyTypes`
    FOREIGN KEY (`frequencyTypes`)
    REFERENCES `medicinehelper`.`durationtypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_patients`
    FOREIGN KEY (`patients_ID`)
    REFERENCES `medicinehelper`.`patients` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_endAfterType`
    FOREIGN KEY (`endAfterType`)
    REFERENCES `medicinehelper`.`durationtypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_startAfterType`
    FOREIGN KEY (`startAfterType`)
    REFERENCES `medicinehelper`.`durationtypes` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schedules_mobileusers`
    FOREIGN KEY (`mobileusers_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `medicinehelper`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicinehelper`.`users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `passwordHash` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(45) NULL DEFAULT NULL,
  `doctor_ID` INT NULL DEFAULT NULL,
  `mobileuser_ID` INT NULL DEFAULT NULL,
  `patient_ID` INT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC),
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC),
  INDEX `fk_users_doctors_idx` (`doctor_ID` ASC),
  INDEX `fk_users_mobileusers_idx` (`mobileuser_ID` ASC),
  INDEX `fk_users_patients_idx` (`patient_ID` ASC),
  CONSTRAINT `fk_users_doctors`
    FOREIGN KEY (`doctor_ID`)
    REFERENCES `medicinehelper`.`doctors` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_mobileuser`
    FOREIGN KEY (`mobileuser_ID`)
    REFERENCES `medicinehelper`.`mobileusers` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_patients`
    FOREIGN KEY (`patient_ID`)
    REFERENCES `medicinehelper`.`patients` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `medicinehelper`.`specializations`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`specializations` (`ID`, `description`) VALUES (1, 'UNG');
INSERT INTO `medicinehelper`.`specializations` (`ID`, `description`) VALUES (2, 'EKG');

COMMIT;


-- -----------------------------------------------------
-- Data for table `medicinehelper`.`doctors`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`doctors` (`ID`, `specializations_ID`) VALUES (11, 1);
INSERT INTO `medicinehelper`.`doctors` (`ID`, `specializations_ID`) VALUES (12, 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `medicinehelper`.`mobileusers`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`mobileusers` (`ID`, `code`) VALUES (21, 'usr1Code');
INSERT INTO `medicinehelper`.`mobileusers` (`ID`, `code`) VALUES (22, 'usr2Code');

COMMIT;


-- -----------------------------------------------------
-- Data for table `medicinehelper`.`durationtypes`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`durationtypes` (`ID`, `value`) VALUES (1, 'HOUR');
INSERT INTO `medicinehelper`.`durationtypes` (`ID`, `value`) VALUES (2, 'DAY');
INSERT INTO `medicinehelper`.`durationtypes` (`ID`, `value`) VALUES (3, 'MONTH');
INSERT INTO `medicinehelper`.`durationtypes` (`ID`, `value`) VALUES (4, 'YEAR');

COMMIT;


-- -----------------------------------------------------
-- Data for table `medicinehelper`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `medicinehelper`;
INSERT INTO `medicinehelper`.`users` (`ID`, `userName`, `passwordHash`, `firstName`, `lastName`, `doctor_ID`, `mobileuser_ID`, `patient_ID`) VALUES (1, 'user1', 'password', 'U1FN', 'U1LN', NULL, 21, NULL);
INSERT INTO `medicinehelper`.`users` (`ID`, `userName`, `passwordHash`, `firstName`, `lastName`, `doctor_ID`, `mobileuser_ID`, `patient_ID`) VALUES (2, 'user2', 'password', 'U2FN', 'U2LN', NULL, 22, NULL);
INSERT INTO `medicinehelper`.`users` (`ID`, `userName`, `passwordHash`, `firstName`, `lastName`, `doctor_ID`, `mobileuser_ID`, `patient_ID`) VALUES (3, 'doctor1', 'password', 'D1FN', 'D1LN', 11, NULL, NULL);
INSERT INTO `medicinehelper`.`users` (`ID`, `userName`, `passwordHash`, `firstName`, `lastName`, `doctor_ID`, `mobileuser_ID`, `patient_ID`) VALUES (4, 'doctor2', 'password', 'D2FN', 'D2LN', 12, NULL, NULL);

COMMIT;

