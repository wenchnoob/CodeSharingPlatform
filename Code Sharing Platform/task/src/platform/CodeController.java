package platform;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

import org.apache.tomcat.util.json.ParseException;

@RestController
public class CodeController {

    @GetMapping(path = "/code/{id}", produces = "text/html; charset=UTF-8")
    public ResponseEntity<String> getCode(@PathVariable int id) {
        Code code = CodeService.getInstance().getCode(id);
        String htmlResponse =
                "<html>" + "<head> <title>Code</title> </head>" +
                        "<body>" +
                        String.format("<pre id=\"code_snippet\">%s</pre>", code.getCode()) +
                        String.format("<span id =\"load_date\">%s</span>", code.getDateCreated()) +
                        "</body>" + "</html>";
        return ResponseEntity.ok().body(htmlResponse);
    }

    @GetMapping(path = "/api/code/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> getApiCode(@PathVariable int id) {
        Code code = CodeService.getInstance().getCode(id);
        String json = String.format("{\"code\":\"%s\",",code.getCode()) +
                String.format("\n\t\"date\":\"%s\"}", code.getDateCreated());
        return ResponseEntity.ok().body(json);
    }

    @PostMapping(path ="/api/code/new", produces="application/json; charset=UTF-8")
    public ResponseEntity<String> setApiCode(@RequestBody String json) {
        JSONParser parser = new JSONParser(json);
        try {
            CodeService.getInstance().makeCode(((LinkedHashMap<String, String>)parser.parse()).getOrDefault("code", "Failed"));
        } catch (ParseException ex) {
            CodeService.getInstance().makeCode("Failed");
        }
        return ResponseEntity.ok().body(String.format("{\"id\":\"%s\"}", CodeService.getInstance().getLatest().getId()));
    }

    @GetMapping(path = "/code/new", produces = "text/html; charset=UTF-8")
    public ResponseEntity<String> getNewCode() {
        String htmlResponse =
                "<html>" + "<head> <title>Create</title> </head>" +
                        "<body>"+
                        "<textarea id=\"code_snippet\"> </textarea>" +
                        "<button id=\"send_snippet\" type=\"submit\" onClick=\"send()\">Submit</button>"+
                        "<script> function send() {\n" +
                        "    let object = {\n" +
                        "        \"code\": document.getElementById(\"code_snippet\").value\n" +
                        "    };\n" +
                        "\n" +
                        "    let json = JSON.stringify(object);\n" +
                        "\n" +
                        "    let xhr = new XMLHttpRequest();\n" +
                        "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                        "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                        "    xhr.send(json);\n" +
                        "\n" +
                        "    if (xhr.status == 200) {\n" +
                        "        alert(\"Success!\");\n" +
                        "    }\n" +
                        "}</script>"+
                        "</body>" + "</html>";
        return ResponseEntity.ok().body(htmlResponse);
    }

    @GetMapping(path = "/api/code/latest", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> getApiLatest() {
        return ResponseEntity.ok().body(JSONIFYLatest(CodeService.getInstance().getLastN(10)));
    }

    @GetMapping(path = "/code/latest", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> getLatest() {
        return ResponseEntity.ok().body(Paginize(CodeService.getInstance().getLastN(10)));
    }

    public String JSONIFYLatest(Iterable<Code> latest) {
        StringBuilder JSON = new StringBuilder();
        JSON.append("[");
        latest.forEach(code -> JSON.append(String.format("\t{\"code\":\"%s\",",code.getCode()) +
                String.format("\n\t\"date\":\"%s\"},\n", code.getDateCreated())));
        JSON.deleteCharAt(JSON.length()-2);
        JSON.append("]");
        return JSON.toString();
    }

    public String Paginize(Iterable<Code> latest) {
        StringBuilder htmlPage = new StringBuilder();
        htmlPage.append("<html> <headh> <title>Latest</title> </head> <body>");
        latest.forEach(code -> htmlPage.append(String.format("<pre id=\"code_snippet\">%s</pre>", code.getCode()) +
                    String.format("<span id =\"load_date\">%s</span>", code.getDateCreated()))
        );
        htmlPage.append("</body></html>");
        return htmlPage.toString();
    }
}
