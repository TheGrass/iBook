package ntu.wg.cq.ibook.pojo;

/**
 * Created by C_Q on 2018/3/29.
 */

public class GameIO {
    private int flag;
    private int id;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public GameIO(){}
    public GameIO(int flag){
        this.flag =flag;
    }
    public GameIO(int flag, String value){
        this.flag =flag;
        this.value =value;
    }
    public GameIO(int flag, int id, String value){
        this.flag =flag;
        this.id = id;
        this.value =value;
    }
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
