package classes;

public enum SchoolYear {

    ING1_GI_GR1("ING1 GI GR1"),
    ING1_GI_GR2("ING1 GI GR2"),
    ING1_GI_GR3("ING1 GI GR3"),
    ING1_GI_GR4("ING1 GI GR4"),
    ING2_GSI_GR1("ING2 GSI GR1"),
    ING2_GSI_GR2("ING2 GSI GR2"),
    ING2_GSI_GR3("ING2 GSI GR3"),
    ING3_AI("ING3 Artificial Intelligence"),
    ING3_BI("ING3 Business Intelligence"),
    ING3_CL("ING3 Cloud Computing"),
    ING3_CY("ING3 Cybersecurity"),
    ING3_ES("ING3 Embedded Systems");

    private String schoolYearValue;

    SchoolYear(String hourValue) { this.schoolYearValue = hourValue; }

    public String getSchoolYearValue() { return schoolYearValue; }

    public void setSchoolYearValue(String newHourValue) { schoolYearValue = newHourValue; }
}
