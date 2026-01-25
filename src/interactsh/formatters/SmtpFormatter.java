package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SMTP-specific formatter.
 */
public class SmtpFormatter extends BaseFormatter {

    public SmtpFormatter() {
        super("smtp");
    }

    @Override
    protected void addProtocolMetadata(StringBuilder sb, JSONObject obj) throws JSONException {
        String smtpFrom = get(obj, "smtp-from");
        if (!smtpFrom.isEmpty()) {
            sb.append(String.format("From Address:\t%s\n", smtpFrom.trim()));
        }
    }

    @Override
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");

        if (!rawRequest.isEmpty()) {
            sb.append("EMAIL CONTENT:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatEmailContent(rawRequest)).append("\n");
        }
    }

    /**
     * Email formatting.
     */
    private String formatEmailContent(String data) {
        if (data.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        String[] lines = data.split("\r?\n");
        boolean inBody = false;

        for (String line : lines) {
            if (!inBody && line.trim().isEmpty()) {
                inBody = true;
                formatted.append("\n--- MESSAGE BODY ---\n");
                continue;
            }

            formatted.append(line).append("\n");
        }

        return formatted.toString();
    }
}