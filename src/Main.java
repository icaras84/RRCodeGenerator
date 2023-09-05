import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.icaras84.rrcodegenerator.core.CodeGenCore;
import com.icaras84.rrcodegenerator.core.CoreUpdate;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.nav.ui.NavigationPanel;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.tools.ui.TimelinePlayer;
import com.icaras84.rrcodegenerator.core.utils.info.EndPoseInfo;
import com.icaras84.rrcodegenerator.core.utils.info.TrajectoryInfo;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;
import com.icaras84.rrcodegenerator.core.utils.robot.RobotComponent;
import com.icaras84.rrcodegenerator.core.utils.robot.RobotPropertyInfo;
import com.icaras84.rrcodegenerator.core.utils.trajectory.trajectoryupdates.GeneralTrajectoryUpdate;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {

        CodeGenCore.initUpdateList();


        //demonstrate trajectory rendering
        CodeGenCore.submitUpdatable(new CoreUpdate() {

            RobotComponent cp = new RobotComponent.BasicCircle(Color.BLACK, Math.sqrt(18 * 18 + 16 * 16) / 2);
            RobotComponent cp2 = new RobotComponent.BasicRectangle(18, 16);

            RobotPropertyInfo properties = new RobotPropertyInfo();
            Trajectory traj = properties.constructTrajectoryBuilder(new Pose2d(0, 0, Math.PI / 2), Math.PI / 2)
                    .splineTo(new Vector2d(30, 30), Math.PI * 0.6)
                    .splineToSplineHeading(new Pose2d(-30, 45, Math.PI), Math.PI)
                    .build();

            TrajectoryInfo trajectoryInfo = new TrajectoryInfo();

            {
                trajectoryInfo.setStartPose(new Pose2d(0, 0, Math.PI / 2));
                trajectoryInfo.setStartTangent(Math.PI / 2);
                trajectoryInfo.add(() -> {
                    EndPoseInfo output = new EndPoseInfo();
                    output.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.splineTo);
                    output.setEndPose(new Pose2d(30, 30));
                    output.setSplineTangent(Math.PI * 0.6);
                    output.setUsingMovementConstraints(false);
                    return output;
                });
                trajectoryInfo.add(() -> {
                    EndPoseInfo output = new EndPoseInfo();
                    output.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.splineToSplineHeading);
                    output.setEndPose(new Pose2d(-30, 45, Math.PI));
                    output.setSplineTangent(Math.PI);
                    return output;
                });
            }

            Stroke normalStroke = new BasicStroke(1);
            Stroke pathStroke = new BasicStroke(3);

            double currentTime = 0;

            @Override
            public void init() {

            }

            @Override
            public void lateInit() {
                TimelinePlayer.setMaxMs(traj.duration() * 1000d);

                NavigationPanel.loadTrajectory(trajectoryInfo);
            }

            @Override
            public void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec) {

            }

            @Override
            public void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec) {
                CanvasRenderer.setColor(Color.BLACK);
                CanvasRenderer.setPenStroke(pathStroke);
                CanvasRenderer.drawTrajectory(traj);
                CanvasRenderer.setColor(Color.GREEN);
                CanvasRenderer.drawPose(traj.start());
                CanvasRenderer.drawPose(traj.end());

                CanvasRenderer.drawPose(traj.get(TimelinePlayer.getCurrentTime()));
                cp.render(Matrix3x3.transform(traj.get(TimelinePlayer.getCurrentTime())));
                cp2.render(Matrix3x3.transform(traj.get(TimelinePlayer.getCurrentTime())));
            }
        });



        //CodeGenCore.submitUpdatable(new GeneralTrajectoryUpdate());

        CodeGenCore.run();
    }
}