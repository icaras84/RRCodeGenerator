import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.icaras84.rrcodegenerator.core.CodeGenCore;
import com.icaras84.rrcodegenerator.core.CoreUpdate;
import com.icaras84.rrcodegenerator.core.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        CodeGenCore.initUpdateList();

        //demonstrate trajectory rendering
        CodeGenCore.submitUpdatable(new CoreUpdate() {

            RobotProperties properties = new RobotProperties();
            Trajectory traj = properties.constructTrajectoryBuilder(new Pose2d(0, 0, Math.PI / 2), Math.PI / 2)
                    .splineTo(new Vector2d(30, 30), Math.PI * 0.6)
                    .splineToSplineHeading(new Pose2d(-30, 45, Math.PI), Math.PI)
                    .build();

            Stroke normalStroke = new BasicStroke(1);
            Stroke pathStroke = new BasicStroke(3);

            @Override
            public void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec) {

            }

            @Override
            public void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec) {
                g.setColor(Color.BLACK);
                g.setStroke(pathStroke);
                CanvasRenderer.drawTrajectory(g, traj);
                g.setColor(Color.GREEN);
                CanvasRenderer.drawPose(g, traj.start());
                CanvasRenderer.drawPose(g, traj.end());
            }
        });

        CodeGenCore.run();
    }
}