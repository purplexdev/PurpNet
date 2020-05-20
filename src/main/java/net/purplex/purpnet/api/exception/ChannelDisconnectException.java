package net.purplex.purpnet.api.exception;

public class ChannelDisconnectException extends RuntimeException {
    public ChannelDisconnectException(final ChannelDisconnectExceptionReason reason) {
        super(ChannelDisconnectExceptionReason.getReason(reason));
    }

    public static enum ChannelDisconnectExceptionReason {
        NOT_CONNECTED, NULL_SERVER_CHANNEL;

        public static String getReason(ChannelDisconnectExceptionReason reason) {
            return (reason == NOT_CONNECTED)
                    ? "You are simply not connected to a server."
                    : "The server channel you are trying to disconnect from is null";
        }
    }
}
