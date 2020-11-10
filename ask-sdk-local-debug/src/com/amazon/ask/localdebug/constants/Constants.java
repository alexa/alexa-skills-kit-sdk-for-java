package com.amazon.ask.localdebug.constants;

/**
 * Class for constants.
 */
public final class Constants {
    /**
     * Private constructor.
     */
    private Constants() {
    }
    // Constants for user provided arguments.
    /**
     * Skill entry class argument name.
     */
    public static final String SKILL_ENTRY_CLASS_ARG_NAME = "skillStreamHandlerClass";
    /**
     * Skill id argument name.
     */
    public static final String SKILL_ID_ARG_NAME = "skillId";
    /**
     * LWA client id argument name.
     */
    public static final String ACCESS_TOKEN = "accessToken";
    /**
     * Thread pool size argument name.
     */
    public static final String THREAD_POOL_SIZE = "threadPoolSize";
    /**`
     * Region argument name.
     */
    public static final String REGION = "region";
    /**
     * Web socket connection uri skeleton.
     */
    public static final String CONNECT_CUSTOM_DEBUG_URI_SKELETON =
            "wss://%1$s/v1/skills/%2$s/stages/development/connectCustomDebugEndpoint";
    // Header constants for web socket connection request.
    /**
     * Upgrade header value.
     */
    public static final String UPGRADE_HEADER_VALUE = "websocket";
    /**
     * Upgrade header key.
     */
    public static final String UPGRADE_HEADER_NAME = "upgrade";
    /**
     * Connection header value.
     */
    public static final String CONNECTION_HEADER_VALUE = "upgrade";
    /**
     * Authorization header key.
     */
    public static final String AUTHORIZATION_HEADER_NAME = "authorization";
    /**
     * Connection header key.
     */
    public static final String CONNECTION_HEADER_NAME = "connection";
    /**
     * Default debugging region. Defaults to North America.
     */
    public static final String DEFAULT_REGION = "NA";
}
