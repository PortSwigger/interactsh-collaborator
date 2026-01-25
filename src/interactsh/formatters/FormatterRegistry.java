package interactsh.formatters;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for protocol formatters.
 */
public class FormatterRegistry {

    private static final Map<String, BaseFormatter> formatters = new HashMap<>();

    static {
        register("dns", new DnsFormatter());
        register("smtp", new SmtpFormatter());
        register("ftp", new FtpFormatter());
        register("ldap", new LdapFormatter());
        register("smb", new SmbFormatter());
        register("responder", new SmbFormatter());

        register("http", new BaseFormatter("http"));
        register("https", new BaseFormatter("https"));
    }

    /**
     * Register a custom formatter.
     */
    public static void register(String protocol, BaseFormatter formatter) {
        formatters.put(protocol.toLowerCase(), formatter);
    }

    /**
     * Get formatter for protocol.
     */
    public static BaseFormatter get(String protocol) {
        if (protocol == null) {
            return new BaseFormatter("unknown");
        }

        BaseFormatter formatter = formatters.get(protocol.toLowerCase());
        if (formatter != null) {
            return formatter;
        }

        BaseFormatter newFormatter = new BaseFormatter(protocol);
        formatters.put(protocol.toLowerCase(), newFormatter);
        return newFormatter;
    }
}