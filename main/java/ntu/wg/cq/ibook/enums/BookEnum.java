package ntu.wg.cq.ibook.enums;

/**
 * Created by C_Q on 2018/3/3.
 */

public enum BookEnum {
    FANTASY(1,"玄幻小说"),
    SCIENCE_FICTION(2,"科幻小说"),
    ONLINE_GAMES(3,"网游小说"),
    HISTORY(4,"历史小说"),
    XIUZHEN(5,"修真小说"),
    URBAN(6,"都市小说"),
    TERROR(7,"恐怖小说"),
    OTHER(8,"其他");

    public Integer clazzId;
    public String clazzName;

    BookEnum(Integer clazzId,String clazzName) {
        this.clazzName=clazzName;
        this.clazzId=clazzId;
    }
}
