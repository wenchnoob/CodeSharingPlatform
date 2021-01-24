package platform;


import java.util.LinkedList;

public class CodeService {
    private static int lastId = 0;
    private static CodeService self;
    private Code latest;
    private LinkedList<Code> codeSnippets = new LinkedList<>();

    private CodeService() { }

    public static CodeService getInstance() {
        if (self != null) return self;
        self = new CodeService();
        return self;
    }

    public Code getCode(int id) {
        return codeSnippets.get(id - 1);
    }

    public Code getLatest() {
        return latest;
    }

    public Code makeCode(String code) {
        latest = new Code(code, ++lastId);
        codeSnippets.add(latest);
        return latest;
    }

    public Iterable<Code> getLastN(int n) {
        LinkedList<Code> lastN = new LinkedList<>();
        int curSize = codeSnippets.size();

        if (n > curSize) {
            while (curSize > 0) {
                lastN.add(codeSnippets.get(--curSize));
            }
        } else {
            int thresh = curSize - n;
            while (thresh < curSize) {
                lastN.add(codeSnippets.get(--curSize));
            }
        }

        return lastN;
    }
    public int size() { return codeSnippets.size(); }
}
