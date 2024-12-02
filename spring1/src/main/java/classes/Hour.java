package classes;

public enum Hour {

    EIGHT_30("8h30"),
    TEN_15("10h15"),
    MIDDAY("12h00"),
    THIRTEEN("13h00"),
    FORTEEN_45("14h45"),
    SIXTEEN_30("16h30"),
    EIGHTEEN_15("18h15");


    private String hourValue;

    Hour(String hourValue) { this.hourValue = hourValue; }

    public String getHourValue() { return hourValue; }

    public void setHourValue(String newHourValue) { hourValue = newHourValue; }

    @Override
    public String toString() { return hourValue; }
}
