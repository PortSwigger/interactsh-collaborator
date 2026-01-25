package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FTP-specific formatter.
 */
public class FtpFormatter extends BaseFormatter {

    public FtpFormatter() {
        super("ftp");
    }

    @Override
    protected String getTitle() {
        return "FTP CONNECTION";
    }

    @Override
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");

        if (!rawRequest.isEmpty()) {
            sb.append("FTP COMMANDS:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatFtpCommands(rawRequest)).append("\n");
        }
    }

    /**
     * Formats FTP commands.
     */
    private String formatFtpCommands(String data) {
        if (data.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        String[] lines = data.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty())
                continue;

            if (trimmed.startsWith("USER ")) {
                formatted.append("\tUSER:\t").append(trimmed.substring(5)).append("\n");
            } else if (trimmed.startsWith("PASS ")) {
                formatted.append("\tPASS:\t").append(maskPassword(trimmed.substring(5))).append("\n");
            } else if (trimmed.startsWith("CWD ")) {
                formatted.append("\tCHDIR:\t").append(trimmed.substring(4)).append("\n");
            } else if (trimmed.startsWith("RETR ")) {
                formatted.append("\tDOWNLOAD:\t").append(trimmed.substring(5)).append("\n");
            } else if (trimmed.startsWith("STOR ")) {
                formatted.append("\tUPLOAD:\t").append(trimmed.substring(5)).append("\n");
            } else if (trimmed.startsWith("LIST") || trimmed.startsWith("NLST")) {
                formatted.append("\tLIST:\t")
                        .append(trimmed.length() > 5 ? trimmed.substring(5) : "(current directory)").append("\n");
            } else {
                formatted.append("  ").append(trimmed).append("\n");
            }
        }

        return formatted.toString();
    }

    /**
     * Masks password.
     */
    private String maskPassword(String password) {
        if (password.isEmpty())
            return "";
        return "********";
    }
}