package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LDAP-specific formatter.
 */
public class LdapFormatter extends BaseFormatter {

    public LdapFormatter() {
        super("ldap");
    }

    @Override
    protected String getTitle() {
        return "LDAP QUERY";
    }

    @Override
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");

        if (!rawRequest.isEmpty()) {
            sb.append("LDAP REQUEST:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatLdapQuery(rawRequest)).append("\n");
        }
    }

    /**
     * Formats LDAP query for readability.
     */
    private String formatLdapQuery(String data) {
        if (data.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        String[] lines = data.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty())
                continue;

            if (trimmed.toLowerCase().contains("baseobject:") || trimmed.toLowerCase().contains("base:")) {
                formatted.append("\tSearch Base:\n");
                formatted.append("\t\t").append(extractValue(trimmed)).append("\n\n");
            } else if (trimmed.toLowerCase().contains("filter:")) {
                formatted.append("\tFilter:\n");
                formatted.append("\t\t").append(extractValue(trimmed)).append("\n\n");
            } else if (trimmed.toLowerCase().contains("scope:")) {
                formatted.append("\tScope:\n");
                formatted.append("\t\t").append(extractValue(trimmed)).append("\n\n");
            } else if (trimmed.toLowerCase().contains("attributes:")) {
                formatted.append("\tAttributes:\n");
                formatted.append("\t\t").append(extractValue(trimmed)).append("\n\n");
            } else {
                formatted.append("  ").append(trimmed).append("\n");
            }
        }

        if (formatted.length() == 0) {
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    formatted.append("  ").append(line.trim()).append("\n");
                }
            }
        }

        return formatted.toString();
    }

    /**
     * Extracts value after colon in LDAP query line.
     */
    private String extractValue(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex > 0 && colonIndex < line.length() - 1) {
            return line.substring(colonIndex + 1).trim();
        }
        return line;
    }
}