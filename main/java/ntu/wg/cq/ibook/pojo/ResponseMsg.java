package ntu.wg.cq.ibook.pojo;

/**
 * Created by C_Q on 2018/2/28.
 */

public class ResponseMsg {
    private MyResponse myResponse;

    public MyResponse getMyResponse() {
        return myResponse;
    }

    public void setMyResponse(MyResponse myResponse) {
        this.myResponse = myResponse;
    }

    public class MyResponse{
        private int state;
        private String message;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
