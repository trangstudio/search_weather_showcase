package core.controller;

public enum Tag {
    Api("@Api"),
    Web("@Web");
    private final String signature;

    Tag(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return signature;
    }
}
