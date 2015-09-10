package bg.unisofia.fmi.valentinalatinova.rest.utils;

public enum Result {
    OK("OK"),
    KO("KO");

    private String value;

    Result(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return generateResult(this.value);
    }

    private String generateResult(String result) {
        return "{ \"result\": \"" + result + "\" }";
    }
}
