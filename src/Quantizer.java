public class Quantizer {
    int start;
    int end;
    int code;
    int qDash;
    public int getqDash() {
        return qDash;
    }



    public void setqDash(int qDash) {
        this.qDash = qDash;
    }



    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void printQuantizer (){
        System.out.println(code + "  " + start + "-> "+end +" qdash " + qDash);
    }
}