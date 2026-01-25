package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DNS-specific formatter.
 */
public class DnsFormatter extends BaseFormatter {

    public DnsFormatter() {
        super("dns");
    }

    @Override
    protected void addProtocolMetadata(StringBuilder sb, JSONObject obj) throws JSONException {
        String queryType = get(obj, "q-type");
        if (!queryType.isEmpty()) {
            sb.append(String.format("Query Type:\t\t%s\n", queryType));
        }
    }

    @Override
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");
        String rawResponse = get(obj, "raw-response");

        if (!rawRequest.isEmpty()) {
            sb.append("DNS REQUEST:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatDnsData(rawRequest)).append("\n\n");
        }

        if (!rawResponse.isEmpty()) {
            sb.append("DNS RESPONSE:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(formatDnsData(rawResponse)).append("\n");
        }
    }

    /**
     * DNS formatting.
     */
    private String formatDnsData(String data) {
        if (data.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        String[] lines = data.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                formatted.append("\n");
            } else if (trimmed.endsWith("SECTION:")) {
                formatted.append("\n").append(trimmed).append("\n");
            } else {
                formatted.append("  ").append(trimmed).append("\n");
            }
        }

        return formatted.toString();
    }
}