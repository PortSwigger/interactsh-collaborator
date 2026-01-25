package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SMB/Responder formatter.
 * Highlights authentication attempts and share access patterns.
 */
public class SmbFormatter extends BaseFormatter {

    public SmbFormatter() {
        super("smb");
    }

    @Override
    protected String getTitle() {
        return "SMB/RESPONDER INTERACTION";
    }

    @Override
    protected String getMetadataSectionTitle() {
        return "CONNECTION INFORMATION";
    }

    @Override
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");

        if (!rawRequest.isEmpty()) {
            sb.append("SMB REQUEST:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatSmbData(rawRequest)).append("\n");
        }
    }

    /**
     * Formats SMB data.
     */
    private String formatSmbData(String data) {
        if (data.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        String[] lines = data.split("\n");

        boolean foundStructuredData = false;

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty())
                continue;

            String lower = trimmed.toLowerCase();

            if (lower.contains("user:") || lower.contains("username:")) {
                formatted.append("\tUsername:\t\t").append(extractValue(trimmed)).append("\n");
                foundStructuredData = true;
            } else if (lower.contains("domain:") || lower.contains("workgroup:")) {
                formatted.append("\tDomain:\t\t").append(extractValue(trimmed)).append("\n");
                foundStructuredData = true;
            } else if (lower.contains("host:") || lower.contains("hostname:")) {
                formatted.append("\tHost:\t\t").append(extractValue(trimmed)).append("\n");
                foundStructuredData = true;
            } else if (lower.contains("share:") || lower.contains("path:")) {
                formatted.append("\tShare/Path:\t\t").append(extractValue(trimmed)).append("\n");
                foundStructuredData = true;
            } else if (lower.contains("hash:") || lower.contains("ntlm:")) {
                formatted.append("\tHash:\t\t").append(maskHash(extractValue(trimmed))).append("\n");
                foundStructuredData = true;
            } else {
                formatted.append("  ").append(trimmed).append("\n");
            }
        }

        if (!foundStructuredData) {
            formatted.setLength(0);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    formatted.append("  ").append(line.trim()).append("\n");
                }
            }
        }

        return formatted.toString();
    }

    /**
     * Extracts value after colon.
     */
    private String extractValue(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex > 0 && colonIndex < line.length() - 1) {
            return line.substring(colonIndex + 1).trim();
        }
        return line;
    }

    /**
     * Masks hash.
     */
    private String maskHash(String hash) {
        if (hash.length() <= 8)
            return "********";
        return hash.substring(0, 4) + "..." + hash.substring(hash.length() - 4);
    }
}