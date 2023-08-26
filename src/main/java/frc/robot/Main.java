/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Данный класс предназначен запуска и выполнения всего проекта
 */
public final class Main
{

  /**
   * Ни в коем случае нельзя изменять метод
   */
  
  public static void main(String... args)
  {
    RobotBase.startRobot(Robot::new);
  }

}
