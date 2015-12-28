package feup.traverse;

/**
 * @author hugof
 * @date 28/12/2015.
 */
public class PhaseDoneItem {

    private String localName;
    private int chapterNumber;
    private int score;

    public PhaseDoneItem(String localName, int chapterNumber, int score) {
        this.localName = localName;
        this.chapterNumber = chapterNumber;
        this.score = score;

    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}