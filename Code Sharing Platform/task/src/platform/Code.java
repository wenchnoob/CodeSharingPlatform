package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Code {
    private int id;
    private LocalDateTime created = LocalDateTime.now();
    private String code;

    public Code(String code, int id) {
        this.id = id;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getId() { return id; }

    public String getDateCreated() {
        return created.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
