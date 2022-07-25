package edu.cmu.cs.cs214.hw4.core.Segments;


/**
 * City segment class
 */
public class CitySegment extends Segment{
    private boolean arms = false;

    /**
     * Construct a city segment
     * @param incomingSegmentType segment type
     */
    public CitySegment(String incomingSegmentType) {
        super(incomingSegmentType);
    }

    /**
     * Get coat of arms of the city segment
     * @return true if this city segment has coat of arms
     *         false if not
     */
    public boolean getArms() { return arms; }

    /**
     * Set coat of arms of the scity segment
     * @param incomingArms coat of arms
     */
    public void setArms(boolean incomingArms){
        arms = incomingArms;
    }

}
