package farrowc.gpsrunner.HighScore;

/**
 * Represents high scores
 * @creation 10/4/2016.
 */
public class HighScores {

    private String deckName;
    private long time;
    private double distance;
    private long id;

    /*
     * @author kuczynskij (10/10/2016)
     */
    @Override
    public String toString(){
        return "Time: "+time+" Distance: "+distance;
    }

    /*
     * @author farrowc (10/11/2016)
     */
    public String toFormattedString(){
        return "Score: " + distance + " Time:" + time;
    }

    /*
     * @author kuczynskij (10/5/2016)
     */
    public long getId() {
        return id;
    }

    /*
     * @author kuczynskij (10/5/2016)
     */
    public void setId(long id) {
        this.id = id;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public double getDistance() {
        return distance;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public String getDeckName() {
        return deckName;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public long getTime() {
        return time;
    }

    /*
     * @author kuczynskij (10/4/2016)
     */
    public void setTime(long time) {
        this.time = time;
    }
}
