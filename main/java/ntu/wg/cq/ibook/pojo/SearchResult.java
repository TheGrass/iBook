package ntu.wg.cq.ibook.pojo;

import java.util.List;

/**
 * Created by C_Q on 2018/3/2.
 */

public class SearchResult {
    private int state;
    private List<Book> message;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Book> getMessage() {
        return message;
    }

    public void setMessage(List<Book> message) {
        this.message = message;
    }
}
