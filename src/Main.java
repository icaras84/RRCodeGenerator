import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.icaras84.rrcodegenerator.core.CodeGenCore;
import com.icaras84.rrcodegenerator.core.CoreUpdate;
import com.icaras84.rrcodegenerator.core.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.utils.info.EndPoseInfo;
import com.icaras84.rrcodegenerator.core.utils.info.RobotPropertyInfo;
import com.icaras84.rrcodegenerator.core.utils.info.TrajectoryInfo;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        /*
        TrajectoryInfo trajectoryInfo = new TrajectoryInfo();
        EndPoseInfo p1 = new EndPoseInfo(trajectoryInfo);
        p1.setEndPose(new Pose2d(30, 30, 0));
        p1.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.lineToConstantHeading);
        EndPoseInfo p2 = new EndPoseInfo(trajectoryInfo);
        p2.setEndPose(new Pose2d(40, 40, Math.PI / 4));
        p2.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.splineToSplineHeading);

        Gson g = (new GsonBuilder())
                .setPrettyPrinting()
                .create();
        String jsonOutput = g.toJson(trajectoryInfo);
        System.out.println(jsonOutput);
        System.out.println();

        Gson g2 = (new GsonBuilder())
                .setPrettyPrinting()
                .create();
        TrajectoryInfo info = g2.fromJson(jsonOutput, TrajectoryInfo.class);
        info.revalidate();
        //info.delete(1);
        System.out.println(g.toJson(info));

        */
        CodeGenCore.initUpdateList();

        //demonstrate trajectory rendering
        CodeGenCore.submitUpdatable(new CoreUpdate() {

            RobotPropertyInfo properties = new RobotPropertyInfo();
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