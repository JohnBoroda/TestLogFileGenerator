import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * @author Eugene Bereskin
 * Created: 16.12.2021 11:52
 */
public class TestLogFileGenerator {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    private static final String templateBeforeDate = "217.138.198.94 - - [";
    private static final String templateAfterDate = " +0300] \"GET /merge/android/access/error?code=612&version=0.9.4.12&platform=11&env=PROD HTTP/1.1\" 200 2 \"-\" \"BestHTTP 1.12.1\"\n";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("usage -- java TestLogFileGenerator.java target_file_name.log");
            return;
        }
        var linesByDay = 300_000;
        var days = 365;
        var date = LocalDate.of(2000, 1, 1);
        var targetFileName = args[0];
        try (var writer = new BufferedWriter(new FileWriter(targetFileName))) {
            for(int i = 0; i <= days; i++) {
                System.out.println(date);
                for(int n = 0; n < linesByDay; n++) {
                    writer.append(templateBeforeDate);
                    //12/Nov/2021:23:11:46
                    var dateTime = LocalDateTime.now()
                            .with(ChronoField.YEAR, date.getYear())
                            .with(ChronoField.MONTH_OF_YEAR, date.getMonthValue())
                            .with(ChronoField.DAY_OF_YEAR, date.getDayOfYear());
                    writer.append(dateTime.format(dateTimeFormatter));
                    writer.append(templateAfterDate);
                }
                date = date.plusDays(1);
                writer.flush();
            }
        }
        System.out.println("done.");
    }
}
