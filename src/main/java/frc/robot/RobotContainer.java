/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.JavaCamera;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.Training;
import frc.robot.logic.InitLogic;

/**
 * Данный класс предназначен для вызова объектов классов, которые необходимо вызвать только один раз,
 * Также для указания класса, который отвечай за реализацию и переключение команд робота
 */
public class RobotContainer
{

  public static Training train;
  public static JavaCamera server;
  public static InitLogic initlogic;

  public RobotContainer()
  {
      initlogic = new InitLogic(StateMachine.commandLogic);
      train = new Training();
      server = new JavaCamera();
      server.Start();

      train.setDefaultCommand(new StateMachine());
  }

}
