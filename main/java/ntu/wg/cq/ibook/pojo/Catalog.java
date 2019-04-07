package ntu.wg.cq.ibook.pojo;

/**
 * Created by C_Q on 2018/3/5.
 */

public class Catalog {
    private int chapterId;
    private String chapterName;
    private String path;
    private Content content;

    public int getChapterId() {
        return chapterId;
    }
    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
    public String getChapterName() {
        return chapterName;
    }
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
