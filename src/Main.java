
import com.icaras84.rrcodegenerator.core.CodeGenCore;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        CodeGenCore.run();
        //CodeGenCanvasRenderer.create();
        /*
        TrajectoryGeneration traj = new TrajectoryGeneration("traj1");
        ArrayList<TrajectoryOperation> ops = new ArrayList<>();

        CodeGenVariable<Pose2d> a = new CodeGenVariable<>();
        a.setReference(true);
        a.setReferenceName("a");
        a.setContent(new Pose2d(0, 0, 0));

        ops.add(new TrajectoryOperation(
                a,
                TrajectoryOperation.TYPE.LINE_TO,
                new CodeGenVariable<>(0d),
                new CodeGenVariable<>(0d)
        ));
        ops.add(new TrajectoryOperation(
                new Pose2d(1, 1, 1),
                TrajectoryOperation.TYPE.SPLINE_TO_AND_CONSTRAINT,
                0,
                0));

        traj.setOperations(ops);
        System.out.println(traj.create());

         */
    }
}