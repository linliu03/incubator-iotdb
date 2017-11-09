package cn.edu.tsinghua.iotdb.qp.physical.crud;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.iotdb.qp.physical.PhysicalPlan;
import cn.edu.tsinghua.iotdb.qp.logical.Operator;
import cn.edu.tsinghua.tsfile.timeseries.read.qp.Path;

/**
 * @author kangrong
 * @author qiaojialin
 */
public class DeletePlan extends PhysicalPlan {
    private long deleteTime;
    private List<Path> paths = new ArrayList<>();

    public DeletePlan() {
        super(false, Operator.OperatorType.DELETE);
    }

    public DeletePlan(long deleteTime, Path path) {
        super(false, Operator.OperatorType.DELETE);
        this.deleteTime = deleteTime;
        this.paths.add(path);
    }

    public DeletePlan(long deleteTime, List<Path> paths) {
        super(false, Operator.OperatorType.DELETE);
        this.deleteTime = deleteTime;
        this.paths = paths;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long delTime) {
        this.deleteTime = delTime;
    }

    public void addPath(Path path) {
        this.paths.add(path);
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    @Override
    public List<Path> getPaths() {
        return paths;
    }

}