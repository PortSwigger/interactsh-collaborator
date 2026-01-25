package interactsh.formatters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Base formatter for protocol interactions.
 */
public class BaseFormatter {

    protected static final String SEPARATOR = "=============";
    protected static final String DIVIDER = "--------------------------";

    private final String protocolName;

    public BaseFormatter(String protocolName) {
        this.protocolName = protocolName;
    }

    /**
     * Formats the interaction.
     */
    public String format(JSONObject obj) throws JSONException {
        StringBuilder sb = new StringBuilder();

        sb.append(SEPARATOR).append("\n");
        sb.append(getTitle()).append("\n");
        sb.append(SEPARATOR).append("\n\n");

        appendMetadata(sb, obj);

        formatContent(sb, obj);

        return sb.toString();
    }

    /**
     * Method to customize formatting for a protocol.
     */
    protected void formatContent(StringBuilder sb, JSONObject obj) throws JSONException {
        String rawRequest = get(obj, "raw-request");
        String rawResponse = get(obj, "raw-response");

        if (!rawRequest.isEmpty()) {
            sb.append("REQUEST:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(rawRequest).append("\n\n");
        }

        if (!rawResponse.isEmpty()) {
            sb.append("RESPONSE:\n");
            sb.append(DIVIDER).append("\n");
            sb.append(rawResponse).append("\n");
        }
    }

    /**
     * Standard metadata section.
     */
    protected void appendMetadata(StringBuilder sb, JSONObject obj) throws JSONException {
        sb.append(getMetadataSectionTitle()).append("\n");
        sb.append(DIVIDER).append("\n");
        sb.append(String.format("Protocol:\t\t%s\n", protocolName.toUpperCase()));
        sb.append(String.format("Source Address:\t%s\n", get(obj, "remote-address")));
        sb.append(String.format("Unique ID:\t\t%s\n", get(obj, "unique-id")));
        sb.append(String.format("Timestamp:\t\t%s\n", get(obj, "timestamp")));

        addProtocolMetadata(sb, obj);

        sb.append("\n");
    }

    /**
     * Override to customize the metadata section title.
     */
    protected String getMetadataSectionTitle() {
        return "METADATA:";
    }

    /**
     * Protocol-specific metadata fields.
     */
    protected void addProtocolMetadata(StringBuilder sb, JSONObject obj) throws JSONException {
    }

    /**
     * The header title.
     */
    protected String getTitle() {
        return protocolName.toUpperCase() + " INTERACTION";
    }

    /**
     * Returns empty string if not found.
     */
    protected String get(JSONObject obj, String key) {
        return obj.optString(key, "");
    }

    /**
     * Getter with default value.
     */
    protected String get(JSONObject obj, String key, String defaultValue) {
        return obj.optString(key, defaultValue);
    }
}