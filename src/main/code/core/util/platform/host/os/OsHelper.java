package core.util.platform.host.os;

public class OsHelper {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    public static String getOsFullName() {
        return OS;
    }

    public static String getOsType() {
        if (isWindows()) return OsType.WIN.toString();
        else if (isUnix()) return OsType.UBUNTU.toString();
        else if (isMac()) return OsType.MAC.toString();
        else return null;
    }

    enum OsType {
        WIN("win"),
        MAC("macos"),
        UBUNTU("ubuntu"),
        ;

        private final String type;

        OsType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }
}
