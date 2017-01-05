package startup.app.speedopt.log;

/**
 * Created by popfisher on 2016/11/29.
 */

public class TimeNoteData {
    /** 时间节点标签 */
    public String tag = "";
    /** 与上一个节点的时间差 */
    public long timeDiff = 0;
    /** 与开始节点的总时间差 */
    public long totalTime = 0;
    /** 标记这个节点到上个节点是不是系统耗时 */
    public boolean isSystemCost = false;
}
