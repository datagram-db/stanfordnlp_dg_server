package uk.ncl.giacomobergami;

import java.util.Date;
import java.util.List;
import org.apache.commons.cli.*;

public class SNLP_Python {

    public String generateGSMDatabase(String start_date, String end_date, List<String> sentences) {
        Date startDate = new Date();
        Date endDate = new Date(Long.MAX_VALUE);
        if ((start_date != null) && (!start_date.isEmpty()))
            startDate = new Date(Date.parse(start_date));
        if ((end_date != null) && (!end_date.isEmpty()))
            endDate = new Date(Date.parse(end_date));
        StringBuilder sb = new StringBuilder();
        GraphRequest.extracted(startDate, endDate, sb, sentences);
        return sb.toString();
    }

    public String getTimeUnits(List<String> sentences) {
        StringBuilder sb = new StringBuilder();
        TimeRequest.extracted(null, null, sb, sentences);
        return sb.toString();
    }

}
